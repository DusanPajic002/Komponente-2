package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {

    private String oldPassword;
    private String password;
    private String token;

    public UpdatePasswordDto(String oldPassword, String password, String token) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.token = token;
    }
}
