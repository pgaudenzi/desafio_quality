package com.desafio.quality.repositories;

import com.desafio.quality.dtos.FlightDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class FlightRepositoryImpl implements FlightRepository {

    private final String flightsDbPath;

    private final DataRepository dataRepository;

    /**
     * Class constructor
     * @param flightsDbPath path to the csv file
     * @param dataRepository repository to load the data from the csv
     */
    public FlightRepositoryImpl(@Value("${flights.db.path}") String flightsDbPath, DataRepository dataRepository) {
        this.flightsDbPath = flightsDbPath;
        this.dataRepository = dataRepository;
    }


    /**
     * Return all the flights in the db.
     * @return list of flights
     */
    @Override
    public List<FlightDto> getAll() {
        List<String[]> flightsData = dataRepository.loadDatabase(flightsDbPath);
        List<FlightDto> flights = new ArrayList<>();

        for (String[] row : flightsData) {
            flights.add(objectMapper(row));
        }

        return flights;
    }

    /**
     * @return all destinations available
     */
    @Override
    public Set<String> getDestinations() {
        final Set<String> destinations = new HashSet<>();
        for (FlightDto flightDto : getAll()) {
            destinations.add(flightDto.getDestination());
        }
        return destinations;
    }

    /**
     * @return all origins available
     */
    @Override
    public Set<String> getOrigins() {
        final Set<String> origins = new HashSet<>();
        for (FlightDto flightDto : getAll()) {
            origins.add(flightDto.getOrigin());
        }
        return origins;
    }

    /**
     * Aux method to get FlightDto objects
     */
    private FlightDto objectMapper(String[] data) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        final String nro = data[0];
        final String origin = data[1];
        final String destination = data[2];
        final String sitType = data[3];
        final int price = Integer.parseInt(data[4].replaceAll("[^0-9]",""));
        final LocalDate departure = LocalDate.parse(data[5], formatter);
        final LocalDate arrival = LocalDate.parse(data[6], formatter);

        return new FlightDto(nro, origin, destination, sitType, price, departure, arrival);
    }

}
