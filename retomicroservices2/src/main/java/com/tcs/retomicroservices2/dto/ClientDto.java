package com.tcs.retomicroservices2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long idclient;
    private String password;
    private Boolean estate;

    // Referencia al objeto PersonDto
    private PersonDto person;

    // Constructor simplificado
    public ClientDto(Long idclient) {
        this.idclient = idclient;
    }
}