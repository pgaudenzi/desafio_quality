package com.desafio.quality.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {

    private String userName;
    private double amount;
    private double interest;
    private double total;
    private BookingDto booking;
    private StatusCodeDto statusCode;

}
