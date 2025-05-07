package com.ms.mal_back.service;

import com.ms.mal_back.dto.admin.UserResponse;
import com.ms.mal_back.dto.admin.AdminUserDetailsResponse;

import java.util.List;

public interface AdminService {
    List<UserResponse> searchUsers(String name, String phone, String role);
    AdminUserDetailsResponse getUserDetails(Long userId);
}
