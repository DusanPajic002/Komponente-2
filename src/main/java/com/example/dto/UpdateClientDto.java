package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateClientDto {


    private String uniqueCardNumber;
    private UserDto userDto;


    @Getter
    @Setter
    public static class UserDto {

        private String username;
        //private String password;
        private String email;
        private String firstName;
        private String lastName;

    }
}
