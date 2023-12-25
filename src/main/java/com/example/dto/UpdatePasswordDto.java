package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {

    private String oldPassword;
    private String password;
    private String email;

    public UpdatePasswordDto(String oldPassword, String password, String email) {
        this.oldPassword = oldPassword;
        this.password = password;
        this.email = email;
    }
}
