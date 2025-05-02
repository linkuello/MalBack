package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditFormRequest {
    private String username;
    private String phone;
    private String email;
    private String password;
}
