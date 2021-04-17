package com.desafio.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusCodeDto {

    private int code;
    private String message;

}
