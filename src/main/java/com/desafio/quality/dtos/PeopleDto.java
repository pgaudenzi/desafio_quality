package com.desafio.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PeopleDto {

    private String dni;
    private String name;
    private String lastname;
    private String birthDate;
    private String mail;

}
