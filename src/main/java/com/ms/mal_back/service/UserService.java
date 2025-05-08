package com.ms.mal_back.service;

import com.ms.mal_back.dto.*;
import com.ms.mal_back.dto.auth.Login;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface UserService {
    UserEditFormResponse getUserEditForm(Long userId);
    UserProfileResponse getUserProfile(Long userId);
    void updateUserProfile(Long userId, UserEditFormRequest request);
    void changePassword(Long userId, String oldPassword, String newPassword);
    String login(Login login);
    void register(UserRequest request) throws Exception;
    void initialRegister(UserRequest request) throws Exception;
    boolean confirmEmail(String token);
    List<AdminUserOverview> getAll();
    public User getUserEntity(Long userId);
    public void requestPasswordReset(String email);
    public void resetPassword(String token, String newPassword);
}
