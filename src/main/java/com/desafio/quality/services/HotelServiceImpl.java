package com.desafio.quality.services;

import com.desafio.quality.dtos.*;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.repositories.HotelRepository;
import com.desafio.quality.utils.DateUtil;
import com.desafio.quality.utils.FilterUtil;
import com.desafio.quality.utils.PaymentUtil;
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
    @Override
    public BookingResponseDto<BookingDto> book(BookingRequestDto<BookingDto> bookingRequest) throws IllegalDateException {
        final BookingDto booking = bookingRequest.getBooking();

        ValidationsUtil.validateLocation(booking.getDestination(), repository.getLocations());

        final List<HotelDto> hotels = FilterUtil.filterHotels(repository.getAll(), booking.getDateFrom(),
                booking.getDateTo(), booking.getDestination());

        final HotelDto hotel = FilterUtil.findHotelByCode(hotels,booking.getHotelCode());
        ValidationsUtil.validateHotel(booking, hotel);

        final double interests = PaymentUtil.calculateInterests(booking.getPaymentMethod());
        final int nights = Period.between(booking.getDateFrom(), booking.getDateTo()).getDays();

        hotel.setBooked("SI");

        BookingResponseDto<BookingDto> response = new BookingResponseDto<>();
        response.setUserName(bookingRequest.getUserName());
        response.setBooking(booking);
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

}
