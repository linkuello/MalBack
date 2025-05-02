package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditFormResponse {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String photoUrl;
}
