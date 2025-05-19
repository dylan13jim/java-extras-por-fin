package com.tcs.retomicroservices2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcs.retomicroservices2.dto.ClientDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idaccount", nullable = false)
    private Long idaccount;

    private String numaccount;
    private String typeaccount;
    private String inibalance;
    private Boolean estateaccount;

    // Relación en la base de datos (solo ID)
    @Column(name = "client_id")
    private Long clientId;

    // Campo transitorio (no persistido) para el DTO
    @Transient
    private ClientDto client;
}