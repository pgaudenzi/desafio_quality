package com.desafio.quality.controllers;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    private List<HotelDto> hotels;
    private HotelController controller;

    @Mock
    private HotelService service;

    @BeforeEach
    void setUp() throws IOException {
        controller = new HotelController(service);
        final ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(
                new File("src/test/resources/test_hotels_db.json"),
                new TypeReference<List<HotelDto>>() {});
    }

    @Test
    void testControllerWithDatesInNull() throws IllegalDateException {
        //When
        when(service.getHotels(any(), any(), any())).thenReturn(hotels);
        final ResponseEntity<List<HotelDto>> result = controller.getHotels(null, null, null);

        //Then
        assertIterableEquals(hotels, result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

}