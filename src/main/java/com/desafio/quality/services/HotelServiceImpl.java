package com.desafio.quality.services;

import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.HotelRepository;
import com.desafio.quality.utils.DateUtil;
import com.desafio.quality.utils.FilterUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service to get the hotels
 */
@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository repository;

    /**
     * Constructor
     * @param repository to get the hotel's data
     */
    public HotelServiceImpl(HotelRepository repository) {
        this.repository = repository;
    }

    /**
     * Get hotels.
     * If the params are null, then return all the hotels
     * If only some of the params are null, then throw IllegalArgumentException
     * If the dates are specified, and if they are valid, return all the hotels available between the dates.
     * @param dateFrom to filter the hotels
     * @param dateTo to filter the hotels
     * @return list of hotels
     */
    @Override
    public List<HotelDto> getHotels(final String dateFrom, final String dateTo, final String location) throws IllegalDateException {
        List<HotelDto> hotels = repository.getAll();
        if (dateFrom == null && dateTo == null && location == null) {
            return hotels;
        }

        if (dateFrom == null || dateTo == null || location == null) {
            throw new IllegalArgumentException("The parameter to filter the hotels are: dateFrom, dateTo, location");
        }

        validateLocation(location);

        final LocalDate convertedDateFrom = DateUtil.convertToLocalDate(dateFrom);
        final LocalDate convertedDateTo = DateUtil.convertToLocalDate(dateTo);

        if (DateUtil.validate(convertedDateFrom, convertedDateTo)) {
            hotels = FilterUtil.filterHotels(hotels, convertedDateFrom, convertedDateTo, location);
        } else {
            throw new IllegalDateException("'Date from' must be before to 'date to'");
        }

        return hotels;
    }

    private void validateLocation(String location) {
        final Set<String> locations = repository.getLocations();
        if (!locations.contains(location)) throw new IllegalArgumentException("The chosen location does not exist");
    }
}
