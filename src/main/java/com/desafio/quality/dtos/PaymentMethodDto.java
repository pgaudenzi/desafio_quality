package com.desafio.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMethodDto {

    private String type;
    private String number;
    private int dues;

}
