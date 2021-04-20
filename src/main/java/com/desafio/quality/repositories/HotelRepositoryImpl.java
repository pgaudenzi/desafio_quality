package com.desafio.quality.repositories;

import com.desafio.quality.dtos.HotelDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class HotelRepositoryImpl implements HotelRepository {

    private final List<HotelDto> hotels;

    private final String hotelsDbPath;
    private final DataRepository dataRepository;

    /**
     * Constructor
     */
    public HotelRepositoryImpl(@Value("${hotels.db.path}") String hotelsDbPath, DataRepository dataRepository ) {
        this.hotelsDbPath = hotelsDbPath;
        this.dataRepository = dataRepository;
        this.hotels = loadDatabase();
    }

    /**
     * Get all hotels
     * @return List of hotels
     */
    @Override
    public List<HotelDto> getAll() {
        return this.hotels;
    }

    /**
     * @return all locations available
     */
    @Override
    public Set<String> getLocations() {
        final Set<String> locations = new HashSet<>();
        for (HotelDto hotel : hotels) {
            locations.add(hotel.getLocation());
        }
        return locations;
    }

    /**
     * Aux method to get the hotels from the csv
     * @return all the hotels in the csv
     */
    private List<HotelDto> loadDatabase() {
        List<String[]> hotelsData = dataRepository.loadDatabase(hotelsDbPath);
        List<HotelDto> hotels = new ArrayList<>();;

        for (String[] row : hotelsData) {
            hotels.add(objectMapper(row));
        }

        return hotels;
    }

    /**
     * Aux method to convert a csv line to an object
     * @param data to parse
     * @return the parsed object
     */
    private HotelDto objectMapper(String[] data) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String code = data[0];
        String name = data[1];
        String location = data[2];
        String roomType = data[3];
        int price = Integer.parseInt(data[4].replaceAll("[^0-9]",""));
        LocalDate dateFrom = LocalDate.parse(data[5], formatter);
        LocalDate dateTo = LocalDate.parse(data[6], formatter);
        String booked = data[7];

        return new HotelDto(code, name, location, roomType, price, dateFrom, dateTo, booked);

    }

}
