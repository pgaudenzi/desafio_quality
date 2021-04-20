package com.desafio.quality.controllers;

import com.desafio.quality.dtos.BookingDto;
import com.desafio.quality.dtos.BookingRequestDto;
import com.desafio.quality.dtos.BookingResponseDto;
import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.services.HotelService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<HotelDto> hotels;
    private BookingResponseDto<BookingDto> response;
    private BookingRequestDto<BookingDto> request;

    private HotelController controller;

    @Mock
    private HotelService service;

    @BeforeEach
    void setUp() throws IOException {
        controller = new HotelController(service);
        hotels = objectMapper.readValue(
                new File("src/test/resources/test_hotels_db.json"),
                new TypeReference<List<HotelDto>>() {});
        response = objectMapper.readValue(
                new File("src/test/resources/test_booking_response.json"),
                new TypeReference<BookingResponseDto<BookingDto>>() {});
        request = objectMapper.readValue(
                new File("src/test/resources/test_booking_request.json"),
                new TypeReference<BookingRequestDto<BookingDto>>() {});
    }

    @Test
    void testGetHotels() throws IllegalDateException {
        //When
        when(service.getHotels(any(), any(), any())).thenReturn(hotels);
        final ResponseEntity<List<HotelDto>> result = controller.getHotels(null, null, null);

        //Then
        assertIterableEquals(hotels, result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testPostBooking() throws Exception {
        //When
        when(service.book(any())).thenReturn(response);
        final ResponseEntity<BookingResponseDto<BookingDto>> result = controller.performBooking(request);

        //Then
        assertEquals(response, result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

}