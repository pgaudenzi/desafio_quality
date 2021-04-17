package com.desafio.quality.utils;

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

}
