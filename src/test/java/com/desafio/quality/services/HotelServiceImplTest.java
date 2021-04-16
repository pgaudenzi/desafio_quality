package com.desafio.quality.services;

import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.HotelRepository;
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
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    private List<HotelDto> hotels;
    private Set<String> location;
    private HotelService service;

    @Mock
    private HotelRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        service = new HotelServiceImpl(repository);
        final ObjectMapper objectMapper = new ObjectMapper();
        hotels = objectMapper.readValue(
                new File("src/test/resources/test_hotels_db.json"),
                new TypeReference<List<HotelDto>>() {});
        location = initLocations();
    }

    @Test
    void shouldReturnAllHotels() {
        //When
        when(repository.getAll()).thenReturn(hotels);

        //Then
        final List<HotelDto> result = assertDoesNotThrow(() -> service.getHotels(null, null, null));
        assertIterableEquals(result, hotels);
    }

    @Test
    void shouldReturnAllHotelsAvailableBetweenDates() {
        //Given
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final HotelDto h1 = new HotelDto("SE-0001", "Selina", "Bogotá", "Single",
                        3900, LocalDate.parse("23/01/2021", formatter),
                        LocalDate.parse("23/11/2021", formatter), "NO");

        //When
        when(repository.getAll()).thenReturn(hotels);
        when(repository.getLocations()).thenReturn(location);

        //Then
        final List<HotelDto> result = assertDoesNotThrow(() -> service.getHotels("23/10/2021", "20/11/2021", "Bogotá"));
        assertEquals(h1, result.get(0));
    }

    @Test
    void shouldThrowIllegalDateException() {
        //When
        when(repository.getAll()).thenReturn(hotels);
        when(repository.getLocations()).thenReturn(location);

        //Then
        final IllegalDateException e = assertThrows(IllegalDateException.class,
                                    () -> service.getHotels("23/11/2021", "20/10/2021", "Bogotá"));
        assertEquals("'Date from' must be before to 'date to'", e.getMessage());
    }

    @Test
    void shouldThrowDateTimeParseException() {
        //When
        when(repository.getAll()).thenReturn(hotels);
        when(repository.getLocations()).thenReturn(location);

        //Then
        assertThrows(DateTimeParseException.class, () -> service.getHotels("u5/11/2021", "20/10/2021", "Bogotá"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSomeParamIsNotSpecified() {
        //When
        when(repository.getAll()).thenReturn(hotels);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.getHotels("20/10/2021", null, "Bogotá"));
        assertEquals("The parameter to filter the hotels are: dateFrom, dateTo, location", e.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenLocationDoesNotExist() {
        //When
        when(repository.getAll()).thenReturn(hotels);

        //Then
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> service.getHotels("20/10/2021", "23/11/2021", "NonExistingLocation"));
        assertEquals("The chosen location does not exist", e.getMessage());
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