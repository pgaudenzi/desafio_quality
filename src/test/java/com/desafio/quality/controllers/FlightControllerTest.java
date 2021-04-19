package com.desafio.quality.controllers;

import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.services.FlightService;
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
class FlightControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<FlightDto> flights;

    @Mock
    private FlightService service;

    private FlightController controller;

    @BeforeEach
    void setUp() throws IOException {
        controller = new FlightController(service);
        flights = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flights_db.json"),
                new TypeReference<List<FlightDto>>() {});
    }

    @Test
    void testGetFlights() throws IllegalDateException {
        //When
        when(service.getFlights(any(), any(), any(), any())).thenReturn(flights);
        final ResponseEntity<List<FlightDto>> response = controller.getFlights(null, null, null, null);

        //Then
        assertIterableEquals(flights, response.getBody());
        assertEquals(200, response.getStatusCodeValue());

    }
}