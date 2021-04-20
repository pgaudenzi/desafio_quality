package com.desafio.quality.services;

import com.desafio.quality.dtos.*;
import com.desafio.quality.exceptions.AvailabilityException;
import com.desafio.quality.exceptions.IllegalDateException;

import java.util.List;

public interface FlightService {

    List<FlightDto> getFlights(String dateFrom, String dateTo, String origin, String destination) throws IllegalDateException;
    BookingResponseDto<FlightReservationDto> book(BookingRequestDto<FlightReservationDto> request) throws AvailabilityException;

}
