package com.desafio.quality.repositories;

import com.desafio.quality.dtos.HotelDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class HotelRepositoryImpl implements HotelRepository {

    private static final String USERS_DB_PATH = "/src/main/resources/hotels_db.csv";
    private static final String ABS_PATH = new File("").getAbsolutePath();

    private final List<HotelDto> hotels;

    /**
     * Constructor - needed to maintain the hotels in memory and manage them from there
     */
    public HotelRepositoryImpl() {
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
        List<HotelDto> hotels = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader(ABS_PATH + USERS_DB_PATH))
                .withSkipLines(1).build()){

            String[] row;
            while ((row = reader.readNext()) != null) {
                hotels.add(objectMapper(row));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    /**
     * Aux method to convert a csv line to an object
     * @param data to parse
     * @return the parsed object
     */
    private HotelDto objectMapper(String[] data) throws ParseException {
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
