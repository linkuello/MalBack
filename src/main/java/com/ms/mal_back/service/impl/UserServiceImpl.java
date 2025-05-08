package com.ms.mal_back.service.impl;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.*;
import com.ms.mal_back.dto.auth.Login;
import com.ms.mal_back.exception.DuplicateUserException;
import com.ms.mal_back.exception.PasswordMismatchException;
import com.ms.mal_back.mapper.UserMapper;
import com.ms.mal_back.repository.RoleRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.EmailService;
import com.ms.mal_back.service.UserService;
import com.ms.mal_back.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    @Override
    public List<AdminUserOverview> getAll() {
        return userMapper.toAdminDtos(userRepository.findAll());
    }

    @Override
    public void register(UserRequest request) throws Exception {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserException("Email or username already exists");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found")));
        user.setEnabled(true); // Admin registers users as already enabled
        userRepository.save(user);
    }

    @Override
    public void initialRegister(UserRequest request) throws Exception{
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found")));
        user.setEnabled(false); // Wait for email confirmation
        userRepository.save(user);

        String token = jwtService.generateEmailConfirmationToken(user.getEmail());
        String confirmLink = "https://ваш-домен/api/users/confirm-email/" + token;
        String text = "Здравствуйте, %s!\n\nПожалуйста, подтвердите email по ссылке:\n%s";
        emailService.sendEmail(user.getEmail(), "Подтверждение регистрации", String.format(text, user.getUsername(), confirmLink));
    }

    @Override
    public boolean confirmEmail(String token) {
        String email = jwtService.extractEmailFromConfirmationToken(token);
        return userRepository.findByEmail(email)
                .filter(u -> !u.isEnabled())
                .map(u -> {
                    u.setEnabled(true);
                    userRepository.save(u);
                    return true;
                })
                .orElseThrow(() -> new RuntimeException("Invalid or already confirmed token"));
    }

    @Override
    public String login(Login login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
        );

        return jwtService.generateToken(user);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordMismatchException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toProfileDto(user);
    }

    @Override
    @CacheEvict(value = "userProfiles", key = "#userId")
    public void updateUserProfile(Long userId, UserEditFormRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Неверный пароль");
        }
        if (!user.getEmail().equals(request.getEmail())) {
            user.setEmail(request.getEmail());
            user.setEnabled(false);

            String newToken = jwtService.generateEmailConfirmationToken(user.getEmail());
            String confirmLink = "https://ваш-домен/api/users/confirm-email/" + newToken;
            String text = "Здравствуйте, %s!\n\nПожалуйста, подтвердите email по ссылке:\n%s";
            emailService.sendEmail(user.getEmail(), "Подтверждение регистрации", String.format(text, user.getUsername(), confirmLink));
        }
        userMapper.updateEntity(user, request); // Clean update
        userRepository.save(user);
    }

    @Override
    @Cacheable(value = "userProfiles", key = "#userId")
    public UserEditFormResponse getUserEditForm(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toEditFormDto(user);
    }

    @Override
    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("No user registered with that email"));

        String token = jwtService.generatePasswordResetToken(email);
        emailService.sendPasswordResetLink(email, token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        String email = jwtService.extractEmailFromConfirmationToken(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
