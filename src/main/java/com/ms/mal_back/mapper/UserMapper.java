package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.*;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface UserMapper {
    public UserProfileResponse toProfileDto(User user);
    public UserEditFormResponse toEditFormDto(User user);
    List<UserEditFormResponse> toDtoS(List<User> users);
    User toEntity(UserRequest userRequest);
    public User updateEntity(User user, UserEditFormRequest request);
}
