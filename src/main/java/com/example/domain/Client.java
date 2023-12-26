package com.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@Table(indexes = {
//        //@Index(columnList = "unique_card_number", unique = true),
//})
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uniqueCardNumber;
    private int nubmerOfTrainings;

    @Embedded
    private User user;

    public Client(Long id, String uniqueCardNumber, int nubmerOfTrainings, User user) {
        this.id = id;
        this.uniqueCardNumber = uniqueCardNumber;
        this.nubmerOfTrainings = nubmerOfTrainings;
        this.user = user;
    }

    public Client(String uniqueCardNumber, int nubmerOfTrainings, User user) {
        this.uniqueCardNumber = uniqueCardNumber;
        this.nubmerOfTrainings = nubmerOfTrainings;
        this.user = user;
    }

    public Client() {

    }
}
