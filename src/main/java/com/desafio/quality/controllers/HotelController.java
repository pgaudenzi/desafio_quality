package com.desafio.quality.controllers;

import com.desafio.quality.dtos.BookingDto;
import com.desafio.quality.dtos.BookingRequestDto;
import com.desafio.quality.dtos.BookingResponseDto;
import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.services.HotelService;
import com.desafio.quality.utils.ValidationsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HotelController {

    private final HotelService service;

    public HotelController(final HotelService service) {
        this.service = service;
    }

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDto>> getHotels(@RequestParam(required = false) String dateFrom,
                                                    @RequestParam(required = false) String dateTo,
                                                    @RequestParam(required = false) String location) throws IllegalDateException {
        return new ResponseEntity<>(service.getHotels(dateFrom, dateTo, location), HttpStatus.OK);
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingResponseDto<BookingDto>> performBooking(@RequestBody BookingRequestDto<BookingDto> bookingRequest)
            throws IllegalDateException {
        //Params validations
        ValidationsUtil.validateMailFormat(bookingRequest.getUserName());
        ValidationsUtil.validateDates(bookingRequest.getBooking().getDateFrom(), bookingRequest.getBooking().getDateTo());
        ValidationsUtil.validateRoomType(bookingRequest.getBooking().getPeopleAmount(), bookingRequest.getBooking().getRoomType());
        ValidationsUtil.validatePeopleAmount(bookingRequest.getBooking().getPeopleAmount(), bookingRequest.getBooking().getPeople().size());

        return new ResponseEntity<>(service.book(bookingRequest), HttpStatus.OK);
    }

}
