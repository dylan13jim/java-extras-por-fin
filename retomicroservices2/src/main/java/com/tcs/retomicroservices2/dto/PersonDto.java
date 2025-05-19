package com.tcs.retomicroservices2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Long idperson;
    private String nameperson;
    private String genderperson;
    private String ageperson;
    private String identification;
    private String addressperson;
    private String phoneperson;
}