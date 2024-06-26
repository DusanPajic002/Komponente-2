package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hallName;
    private String startDate;
    private final String rola = "Manager";

    @Embedded
    private User user;

    public Manager( String hallName, String startDate, User user) {
        this.hallName = hallName;
        this.startDate = startDate;
        this.user = user;
    }

    public Manager() {

    }
}
