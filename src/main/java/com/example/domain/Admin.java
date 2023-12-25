package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "username", unique = true)
})
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private User user;

    public Admin(User user) {
        this.user = user;
    }

    public Admin() {

    }
}
