package com.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long uniqueCardNumber;
    private static long i = 100;
    private int nubmerOfTrainings;
    private final String rola = "Client";
    @Embedded
    private User user;

    @PrePersist
    protected void onPrePersist() {
        this.uniqueCardNumber = i;
       i++;
    }

    public Client(Long id, int nubmerOfTrainings, User user) {
        this.id = id;
        this.nubmerOfTrainings = nubmerOfTrainings;
        this.user = user;
    }

    public Client() {

    }
}
