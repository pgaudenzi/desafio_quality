package com.desafio.quality.services;

import com.desafio.quality.dtos.*;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.HotelRepository;
import com.desafio.quality.utils.DateUtil;
import com.desafio.quality.utils.FilterUtil;
import com.desafio.quality.utils.ValidationsUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

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

        ValidationsUtil.validateLocation(location, repository.getLocations());
        final LocalDate convertedDateFrom = DateUtil.convertToLocalDate(dateFrom);
        final LocalDate convertedDateTo = DateUtil.convertToLocalDate(dateTo);
        ValidationsUtil.validateDates(convertedDateFrom, convertedDateTo);

        hotels = FilterUtil.filterHotels(hotels, convertedDateFrom, convertedDateTo, location);

        return hotels;
    }

    /**
     * Perform the booking, only if all the data in the request is correct.
     * @param bookingRequest the request to booking
     * @return the response to the request
     * @throws IllegalDateException if the dates are wrong
     */
    //TODO: ver como hacer para la fecha con formato dd/mm/yyyy
    @Override
    public BookingResponseDto book(BookingRequestDto bookingRequest) throws IllegalDateException {
        final BookingDto booking = bookingRequest.getBooking();

        validateBookingParams(bookingRequest);

        final HotelDto hotel = FilterUtil.findHotelByCode(repository.getAll(),booking.getHotelCode());
        ValidationsUtil.validateHotel(booking, hotel);

        final double interests = calculateInterests(booking.getPaymentMethod());
        final int nights = Period.between(booking.getDateFrom(), booking.getDateTo()).getDays();

        hotel.setBooked("SI");

        BookingResponseDto response = new BookingResponseDto();
        response.setUserName(bookingRequest.getUserName());
        response.setBooking(bookingRequest.getBooking());
        response.setAmount((double) hotel.getPrice() * nights);
        response.setInterest(interests);

        if (booking.getPaymentMethod().getType().equalsIgnoreCase("debit")) {
            response.setTotal(response.getAmount());
        } else {
            response.setTotal(response.getAmount() * response.getInterest());
        }

        final StatusCodeDto status = new StatusCodeDto(200, "The process ended satisfactorily");
        response.setStatusCode(status);

        return response;
    }

    /**
     * Aux method to calculate card interest
     */
    private double calculateInterests(PaymentMethodDto paymentMethod) {

        if (paymentMethod.getType().equalsIgnoreCase("credit")) {
            if (paymentMethod.getDues() < 3) return  1.05;
            if (paymentMethod.getDues() >= 3 && paymentMethod.getDues() <= 6) return 1.10;
            throw new IllegalArgumentException("Max dues allowed is 6");
        }

        if (paymentMethod.getType().equalsIgnoreCase("debit")) {
            if (paymentMethod.getDues() != 0)
                throw new IllegalArgumentException("dues are not allowed with debit card");
            return 0.0;
        }

        throw new IllegalArgumentException("Only credit or debit allowed");
    }

    /**
     * Aux method to perform all the validations needed
     */
    private void validateBookingParams(final BookingRequestDto bookingRequest) throws IllegalDateException {
        ValidationsUtil.validateMailFormat(bookingRequest.getUserName());
        ValidationsUtil.validateLocation(bookingRequest.getBooking().getDestination(), repository.getLocations());

        ValidationsUtil.validateDates(bookingRequest.getBooking().getDateFrom(),
                bookingRequest.getBooking().getDateTo());

        ValidationsUtil.validateRoomType(bookingRequest.getBooking().getPeopleAmount(),
                bookingRequest.getBooking().getRoomType());
    }
}
