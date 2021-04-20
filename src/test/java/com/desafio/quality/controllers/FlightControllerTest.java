package com.desafio.quality.controllers;

import com.desafio.quality.dtos.BookingRequestDto;
import com.desafio.quality.dtos.BookingResponseDto;
import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.dtos.FlightReservationDto;
import com.desafio.quality.exceptions.AvailabilityException;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<FlightDto> flights;
    private BookingRequestDto<FlightReservationDto> request;
    private BookingResponseDto<FlightReservationDto> response;

    @Mock
    private FlightService service;

    private FlightController controller;

    @BeforeEach
    void setUp() throws IOException {
        controller = new FlightController(service);
        flights = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flights_db.json"),
                new TypeReference<List<FlightDto>>() {});
        request = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flight_reservation_request.json"),
                new TypeReference<BookingRequestDto<FlightReservationDto>>() {});
        response = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flight_reservation_response.json"),
                new TypeReference<BookingResponseDto<FlightReservationDto>>() {});
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

    @Test
    void testReservationRequest() throws AvailabilityException {
        //When
        when(service.book(request)).thenReturn(response);

        //Then
        final ResponseEntity<BookingResponseDto<FlightReservationDto>> result = assertDoesNotThrow(() -> controller.bookFlight(request));
        assertEquals(response, result.getBody());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidMailFormat() throws AvailabilityException {
        //Given
        request.setUserName("no_valid@username");

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> controller.bookFlight(request));
        assertEquals("Invalid email format", e.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPeopleAmountIsNotRight() throws AvailabilityException {
        //Given
        request.getBooking().setSeats(3);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> controller.bookFlight(request));
        assertEquals("You asked a seat for 3 and there are 2 people in the reservation", e.getMessage());
    }

    @Test
    void shouldThrowIllegalDateExceptionWhenDateFromIsAfterDateTo() throws AvailabilityException {
        //Given
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        request.getBooking().setDateFrom(LocalDate.parse("14/02/2021", formatter));
        request.getBooking().setDateTo(LocalDate.parse("10/02/2021", formatter));

        //Then
        final IllegalDateException e = assertThrows(IllegalDateException.class, () -> controller.bookFlight(request));
        assertEquals("'Date from' must be before to 'date to'", e.getMessage());
    }
}