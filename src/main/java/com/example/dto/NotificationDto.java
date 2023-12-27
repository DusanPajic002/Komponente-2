package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto {

    private String username;

    public NotificationDto() {
    }

    public NotificationDto(String username) {
        this.username = username;
    }
}

