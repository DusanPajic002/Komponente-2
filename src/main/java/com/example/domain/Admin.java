package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private User user;
    private final String rola = "Admin";

    public Admin(User user) {
        this.user = user;
    }

    public Admin() {

    }
}
