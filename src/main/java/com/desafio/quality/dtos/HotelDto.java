package com.desafio.quality.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    private String code;
    private String name;
    private String location;
    private String roomType;
    private int price;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateFrom;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateTo;
    private String booked;

}
