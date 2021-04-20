package com.desafio.quality.utils;

import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.dtos.HotelDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilterUtil {

    private FilterUtil() {}

    /**
     * Get all the hotels with available rooms between dateFrom and dateTo
     * @param dateFrom date from
     * @param dateTo date to
     * @return list of hotels
     */
    public static List<HotelDto> filterHotels(List<HotelDto> hotels, LocalDate dateFrom, LocalDate dateTo, String location) {
        return hotels.stream()
                .filter(hotel -> (dateFrom.isAfter(hotel.getDateFrom()) || dateFrom.equals(hotel.getDateFrom()))
                        && (dateTo.isBefore(hotel.getDateTo()) || dateTo.equals(hotel.getDateTo()))
                        && hotel.getLocation().equals(location)
                        && hotel.getBooked().equals("NO"))
                .collect(Collectors.toList());
    }

    /**
     * Find hotel by its code
     * @param hotels the list of hotel
     * @param code hotel
     */
    public static HotelDto findHotelByCode(final List<HotelDto> hotels, final String code) {
        Optional<HotelDto> result = hotels.stream().filter(hotel -> hotel.getCode().equals(code)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new IllegalArgumentException("The hotel with code: " + code + " does not exist");
        }
    }

    /**
     * Filters flights according to the params
     * @return filtered flights
     */
    public static List<FlightDto> filterFlights(List<FlightDto> flights, LocalDate dateFrom, LocalDate dateTo,
                                                      String origin, String destination) {
        return flights.stream()
                .filter(flight -> (dateFrom.isAfter(flight.getDeparture()) || dateFrom.equals(flight.getDeparture()))
                        && (dateTo.isBefore(flight.getArrival()) || dateTo.equals(flight.getArrival()))
                        && flight.getDestination().equalsIgnoreCase(destination)
                        && flight.getOrigin().equalsIgnoreCase(origin))
                .collect(Collectors.toList());
    }

    public static FlightDto findFlightByNumber(final List<FlightDto> flights, final String nro) {
        Optional<FlightDto> result = flights.stream().filter(flight -> flight.getNro().equals(nro)).findFirst();
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new IllegalArgumentException("The flight with number: " + nro + " does not exist");
        }
    }

}
