package com.desafio.quality.services;

import com.desafio.quality.dtos.BookingRequestDto;
import com.desafio.quality.dtos.BookingResponseDto;
import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;

import java.util.List;

public interface HotelService {

    List<HotelDto> getHotels(String dateFrom, String dateTo, String location) throws IllegalDateException;
    BookingResponseDto book(BookingRequestDto bookingRequest) throws IllegalDateException;

}
