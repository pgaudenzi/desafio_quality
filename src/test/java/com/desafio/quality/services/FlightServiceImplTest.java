package com.desafio.quality.services;

import com.desafio.quality.dtos.BookingRequestDto;
import com.desafio.quality.dtos.BookingResponseDto;
import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.dtos.FlightReservationDto;
import com.desafio.quality.exceptions.AvailabilityException;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.FlightRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<FlightDto> flights;
    private Set<String> locations;
    private BookingRequestDto<FlightReservationDto> request;
    private BookingResponseDto<FlightReservationDto> response;

    private FlightService service;

    @Mock
    FlightRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        service = new FlightServiceImpl(repository);
        flights = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flights_db.json"),
                new TypeReference<List<FlightDto>>() {});
        locations = initLocations();
        request = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flight_reservation_request.json"),
                new TypeReference<BookingRequestDto<FlightReservationDto>>() {});
        response = objectMapper.readValue(
                new File("src/test/resources/tests_flights/test_flight_reservation_response.json"),
                new TypeReference<BookingResponseDto<FlightReservationDto>>() {});
    }

    @Test
    void shouldReturnAllFlights() {
        //When
        when(repository.getAll()).thenReturn(flights);

        //Then
        final List<FlightDto> result = assertDoesNotThrow(() -> service.getFlights(null, null, null, null));
        assertIterableEquals(flights, result);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSomeFiltersAreNull() {
        //When
        when(repository.getAll()).thenReturn(flights);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.getFlights("16/02/2021", "28/02/2021", null, "Buenos Aires"));
        assertEquals("The parameter to filter the hotels are: dateFrom, dateTo, origin and destination",
                e.getMessage());
    }

    @Test
    void shouldReturnFilteredFlights() {
        //Given
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final FlightDto expectedFlight = new FlightDto();
        expectedFlight.setNro("BOBA-6567");
        expectedFlight.setOrigin("Bogotá");
        expectedFlight.setDestination("Buenos Aires");
        expectedFlight.setSitType("Business");
        expectedFlight.setPrice(57000);
        expectedFlight.setDeparture(LocalDate.parse("15/02/2021", formatter));
        expectedFlight.setArrival(LocalDate.parse("28/02/2021", formatter));

        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getDestinations()).thenReturn(locations);
        when(repository.getOrigins()).thenReturn(locations);

        //Then
        List<FlightDto> result = assertDoesNotThrow(() -> service.getFlights("16/02/2021", "28/02/2021",
                "Bogotá", "Buenos Aires"));
        assertEquals(1, result.size());
        assertEquals(expectedFlight, result.get(0));

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDestinationDoesNotExist() {
        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getOrigins()).thenReturn(locations);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.getFlights("16/02/2021", "28/02/2021",
                        "Non existing", "Buenos Aires"));
        assertEquals("The chosen location does not exist", e.getMessage());
    }

    @Test
    void shouldThrowIllegalDateExceptionWhenDateToComesFirst() {
        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getOrigins()).thenReturn(locations);
        when(repository.getDestinations()).thenReturn(locations);

        //Then
        final IllegalDateException e = assertThrows(IllegalDateException.class,
                () -> service.getFlights("28/02/2021", "16/02/2021",
                        "Bogotá", "Buenos Aires"));
        assertEquals("'Date from' must be before to 'date to'", e.getMessage());
    }

    @Test
    void shouldPerformFlightReservationRequest() {
        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getOrigins()).thenReturn(locations);
        when(repository.getDestinations()).thenReturn(locations);

        //Then
        final BookingResponseDto<FlightReservationDto> result = assertDoesNotThrow(() -> service.book(request));
           assertEquals(response, result);
    }

    @Test
    void shouldPerformFlightReservationRequestWithDebit() {
        //Given
        request.getBooking().getPaymentMethod().setType("DEBIT");
        request.getBooking().getPaymentMethod().setDues(0);
        response.getBooking().getPaymentMethod().setType("DEBIT");
        response.getBooking().getPaymentMethod().setDues(0);
        response.setTotal(13000.0);
        response.setInterest(0.0);

        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getOrigins()).thenReturn(locations);
        when(repository.getDestinations()).thenReturn(locations);

        //Then
        final BookingResponseDto<FlightReservationDto> result = assertDoesNotThrow(() -> service.book(request));
        assertEquals(response, result);
    }


    @Test
    void shouldThrowAvailabilityExceptionWhenThereAreNoFlightForThePeriodSpecified() throws AvailabilityException {
        //Given
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        request.getBooking().setDateFrom(LocalDate.parse("10/11/2021", formatter));
        request.getBooking().setDateTo(LocalDate.parse("20/11/2021", formatter));

        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getDestinations()).thenReturn(locations);
        when(repository.getOrigins()).thenReturn(locations);

        //Then
        final AvailabilityException e = assertThrows(AvailabilityException.class, () -> service.book(request));
        assertEquals("There are no flights available for the period specified", e.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFlightNumberDoesNotExist() {
        //Given
        request.getBooking().setFlightNumber("non-existing-number");

        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getDestinations()).thenReturn(locations);
        when(repository.getOrigins()).thenReturn(locations);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.book(request));
        assertEquals("The flight with number: non-existing-number does not exist", e.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenParametersDoNotMatchWithTheFlightData() {
        //Given
        request.getBooking().setSeatType("some class");

        //When
        when(repository.getAll()).thenReturn(flights);
        when(repository.getDestinations()).thenReturn(locations);
        when(repository.getOrigins()).thenReturn(locations);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.book(request));
        assertEquals("The parameters do not match with the flight data", e.getMessage());
    }

    private Set<String> initLocations() {
        final Set<String> locations = new HashSet<>();
        locations.add("Puerto Iguazú");
        locations.add("Buenos Aires");
        locations.add("Tucumán");
        locations.add("Bogotá");
        locations.add("Medellín");
        locations.add("Cartagena");

        return locations;
    }
}