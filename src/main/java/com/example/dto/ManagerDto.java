package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDto {

    private Long id;
    private String hallName;
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public ManagerDto(Long id, String hallName, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.hallName = hallName;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
