package com.tcs.retomicroservices.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idperson", nullable = false)
    private Long idperson;

    private String nameperson;

    private String genderperson;

    private String ageperson;

    @Column(length = 10, unique = true)
    private String identification;

    private String addressperson;

    @Column(length = 10)
    private String phoneperson;
}
