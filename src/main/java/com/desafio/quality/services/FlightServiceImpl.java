package com.desafio.quality.services;

import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.FlightRepository;
import com.desafio.quality.utils.DateUtil;
import com.desafio.quality.utils.FilterUtil;
import com.desafio.quality.utils.ValidationsUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository repository;

    public FlightServiceImpl(FlightRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FlightDto> getFlights(String dateFrom, String dateTo, String origin, String destination)
            throws IllegalDateException {
        List<FlightDto> flights = repository.getAll();
        if (dateFrom == null && dateTo == null && origin == null && destination == null) {
            return flights;
        }

        if (dateFrom == null || dateTo == null || origin == null || destination == null) {
            throw new IllegalArgumentException("The parameter to filter the hotels are: dateFrom, dateTo, origin and destination");
        }

        ValidationsUtil.validateLocation(origin, repository.getOrigins());
        ValidationsUtil.validateLocation(destination, repository.getDestinations());
        final LocalDate convertedDateFrom = DateUtil.convertToLocalDate(dateFrom);
        final LocalDate convertedDateTo = DateUtil.convertToLocalDate(dateTo);
        ValidationsUtil.validateDates(convertedDateFrom, convertedDateTo);

        flights = FilterUtil.filterFlights(flights, convertedDateFrom, convertedDateTo, origin, destination);

        return flights;
    }
}
