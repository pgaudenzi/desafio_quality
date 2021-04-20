package com.desafio.quality.services;

import com.desafio.quality.dtos.*;
import com.desafio.quality.exceptions.AvailabilityException;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.FlightRepository;
import com.desafio.quality.utils.DateUtil;
import com.desafio.quality.utils.FilterUtil;
import com.desafio.quality.utils.PaymentUtil;
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

    /**
     * Get flights.
     * If there are no params, all flights are return
     * If only some of the params are null, then throw IllegalArgumentException
     * If the dates are specified, and if they are valid, return all the flights available between the dates.
     */
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

    /**
     * Process a reservation request
     */
    @Override
    public BookingResponseDto<FlightReservationDto> book(BookingRequestDto<FlightReservationDto> request) throws AvailabilityException {
        final FlightReservationDto reservation = request.getBooking();

        //Validate locations
        ValidationsUtil.validateLocation(reservation.getOrigin(), repository.getOrigins());
        ValidationsUtil.validateLocation(reservation.getDestination(), repository.getDestinations());

        //Filter flights according params
        final List<FlightDto> flights = FilterUtil.filterFlights(repository.getAll(), reservation.getDateFrom(),
                reservation.getDateTo(), reservation.getOrigin(), reservation.getDestination());

        if (flights.isEmpty()) {
            throw new AvailabilityException("There are no flights available for the period specified");
        }

        //Validate flights params
        final FlightDto flight = FilterUtil.findFlightByNumber(flights, reservation.getFlightNumber());
        ValidationsUtil.validateFlight(reservation, flight);

        return buildResponse(request.getUserName(), flight, reservation);
    }

    /**
     * Aux method to build a reservation response
     */
    private BookingResponseDto<FlightReservationDto> buildResponse(String userName, final FlightDto flight,
                                                                   final FlightReservationDto reservation) {
        final BookingResponseDto<FlightReservationDto> response = new BookingResponseDto<>();
        final double interests = PaymentUtil.calculateInterests(reservation.getPaymentMethod());

        response.setUserName(userName);
        response.setAmount((double) flight.getPrice() * reservation.getSeats());
        response.setInterest(interests);

        if (reservation.getPaymentMethod().getType().equalsIgnoreCase("debit")) {
            response.setTotal(response.getAmount());
        } else {
            response.setTotal(Math.round(response.getAmount() * response.getInterest()));
        }

        response.setBooking(reservation);

        final StatusCodeDto status = new StatusCodeDto(200, "The process ended satisfactorily");
        response.setStatusCode(status);

        return response;
    }

}
