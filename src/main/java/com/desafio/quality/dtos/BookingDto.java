package com.desafio.quality.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateFrom;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dateTo;
    private String destination;
    private String hotelCode;
    private int peopleAmount;
    private String roomType;
    private List<PeopleDto> people;
    private PaymentMethodDto paymentMethod;

}
