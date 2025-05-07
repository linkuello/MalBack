package com.ms.mal_back.controller;

import com.ms.mal_back.dto.admin.AdminUserDetailsResponse;
import com.ms.mal_back.dto.admin.UserResponse;
import com.ms.mal_back.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 🔍 Поиск и фильтрация пользователей
    @GetMapping("/users")
    public List<UserResponse> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role
    ) {
        return adminService.searchUsers(name, phone, role);
    }

    // 👤 Детали пользователя
    @GetMapping("/users/{id}")
    public AdminUserDetailsResponse getUserDetails(@PathVariable Long id) {
        return adminService.getUserDetails(id);
    }
}
