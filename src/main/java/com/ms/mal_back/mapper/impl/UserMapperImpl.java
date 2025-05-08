package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.config.UrlBuilder;
import com.ms.mal_back.dto.*;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.AdvertisementMapper;
import com.ms.mal_back.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final AdvertisementMapper advertisementMapper;
    private final UrlBuilder urlBuilder;

    @Override
    public UserProfileResponse toProfileDto(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setPhotoUrl(user.getPhoto() != null ? urlBuilder.buildFullPhotoUrl(user.getPhoto().getFilePath()) : null);
        response.setAdvertisements(user.getAdvertisements().stream()
                .map(advertisementMapper::toSimpleDto)
                .collect(Collectors.toList()));
        return response;
    }

    @Override
    public UserEditFormResponse toEditFormDto(User user) {
        UserEditFormResponse response = new UserEditFormResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setPhotoUrl(user.getPhoto() != null ? urlBuilder.buildFullPhotoUrl(user.getPhoto().getFilePath()) : null);
        return response;
    }

    @Override
    public List<AdminUserOverview> toAdminDtos(List<User> users) {
        return users.stream()
                .map(this::toAdminDto)
                .collect(Collectors.toList());
    }
    @Override
    public AdminUserOverview toAdminDto (User user) {
        AdminUserOverview adminUserOverview = new AdminUserOverview();
        adminUserOverview.setId(user.getId());
        adminUserOverview.setUsername(user.getUsername());
        adminUserOverview.setPhone(user.getPhone());
        adminUserOverview.setEmail(user.getEmail());
        adminUserOverview.setEnabled(user.isEnabled());
        return adminUserOverview;
    }

    @Override
    public List<UserEditFormResponse> toDtoS(List<User> users) {
        return users.stream()
                .map(this::toEditFormDto)
                .collect(Collectors.toList());
    }

    @Override
    public User toEntity(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        return user;
    }

    @Override
    public User updateEntity(User user, UserEditFormRequest request) {
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        return user;
    }
}
