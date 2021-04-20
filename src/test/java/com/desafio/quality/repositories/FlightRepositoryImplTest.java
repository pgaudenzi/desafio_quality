package com.desafio.quality.repositories;

import com.desafio.quality.dtos.FlightDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightRepositoryImplTest {

    private FlightRepository repository;

    @Mock
    private DataRepository dataRepository;

    @BeforeEach
    void setUp() {
        repository = new FlightRepositoryImpl("src/test/resources/tests_flights/test_flights_db.json",
                                                dataRepository);
    }

    @Test
    void shouldGetAllFlights() {
        //Given
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final String[] d1 = {"CH-0002", "Cataratas Hotel", "Puerto Iguazu", "Economy", "$6500", "10/02/2021", "15/02/2021"};
        final String[] d2 = {"CH-0003", "Cataratas Hotel", "Puerto Iguazu", "Economy", "$6500", "10/02/2021", "15/02/2021"};
        final List<String[]> data = new ArrayList<>();
        data.add(d1);
        data.add(d2);

        final FlightDto f1 = new FlightDto("CH-0002", "Cataratas Hotel", "Puerto Iguazu", "Economy",
                6500, LocalDate.parse("10/02/2021", formatter), LocalDate.parse("15/02/2021", formatter));
        final FlightDto f2 = new FlightDto("CH-0003", "Cataratas Hotel", "Puerto Iguazu", "Economy",
                6500, LocalDate.parse("10/02/2021", formatter), LocalDate.parse("15/02/2021", formatter));
        final List<FlightDto> flights = new ArrayList<>();
        flights.add(f1);
        flights.add(f2);

        //When
        when(dataRepository.loadDatabase(any())).thenReturn(data);
        final List<FlightDto> result = repository.getAll();

        //Then
        assertEquals(2, flights.size());
        assertIterableEquals(flights, result);
    }
}