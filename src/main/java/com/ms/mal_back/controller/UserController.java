package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.*;
import com.ms.mal_back.dto.auth.Login;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.service.PhotoService;
import com.ms.mal_back.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;
    private final PhotoService photoService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/my")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit-form")
    public ResponseEntity<UserEditFormResponse> getEditForm(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(userService.getUserEditForm(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart("user") UserEditFormRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        userService.updateUserProfile(userId, request);
        if (photo != null && !photo.isEmpty()) {
            User user = userService.getUserEntity(userId);
            photoService.replaceProfilePhoto(user, photo);
        }
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<Void> registerAsAdmin(@RequestBody UserRequest request,@RequestHeader("Authorization") String token) throws Exception {
        jwtService.validateToken(token);
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> initialRegister(@RequestBody UserRequest request) throws Exception {
        userService.initialRegister(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirm-email/{token}")
    public ResponseEntity<String> confirmEmail(@PathVariable String token) {
        boolean confirmed = userService.confirmEmail(token);
        return confirmed
                ? ResponseEntity.ok("Email confirmed successfully!")
                : ResponseEntity.badRequest().body("Invalid or already confirmed token.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        String token = userService.login(login);
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<Void> requestReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}
