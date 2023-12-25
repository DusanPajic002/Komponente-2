package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePermissionDto {

    private String email;
    private String username;
    private boolean permission;

    public UpdatePermissionDto(String email, String username, boolean permission) {
        this.email = email;
        this.username = username;
        this.permission = permission;
    }
}
