package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.*;
import com.ms.mal_back.dto.auth.Login;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.service.PhotoService;
import com.ms.mal_back.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;
    private final PhotoService photoService;

    @Operation(summary = "Get profile of the currently authenticated user")
    @GetMapping("/profile/my")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @Operation(summary = "Get public profile of another user by ID")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @Operation(summary = "Get form data to edit current user's profile")
    @GetMapping("/edit-form")
    public ResponseEntity<UserEditFormResponse> getEditForm(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(userService.getUserEditForm(userId));
    }

    @Operation(summary = "Update user's profile data and photo")
    @PutMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserProfile(
            @Parameter(hidden = true)
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

    @Operation(summary = "Register a new admin user (admin-only access)")
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> registerAsAdmin(
            @RequestBody UserRequest request
    ) throws Exception {
        userService.register(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Register a new regular user")
    @PostMapping("/register")
    public ResponseEntity<Void> initialRegister(
            @RequestBody UserRequest request
    ) throws Exception {
        userService.initialRegister(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Confirm email via token")
    @GetMapping("/confirm-email/{token}")
    public ResponseEntity<String> confirmEmail(
            @PathVariable String token
    ) {
        boolean confirmed = userService.confirmEmail(token);
        return confirmed
                ? ResponseEntity.ok("Email confirmed successfully!")
                : ResponseEntity.badRequest().body("Invalid or already confirmed token.");
    }

    @Operation(summary = "Login and receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody Login login
    ) {
        String token = userService.login(login);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Change password for authenticated user")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok().build();
    }
}
