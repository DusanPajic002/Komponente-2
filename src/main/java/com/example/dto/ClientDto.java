package com.example.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String uniqueCardNumber;
    private int nubmerOfTrainings;

    public ClientDto(Long id, String username, String email, String firstName, String lastName, String uniqueCardNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.uniqueCardNumber = uniqueCardNumber;
    }
}
