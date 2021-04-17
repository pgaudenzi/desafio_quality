package com.desafio.quality.controllers;

import com.desafio.quality.dtos.BookingRequestDto;
import com.desafio.quality.dtos.BookingResponseDto;
import com.desafio.quality.dtos.HotelDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.services.HotelService;
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
    public ResponseEntity<BookingResponseDto> performBooking(@RequestBody BookingRequestDto bookingRequest)
            throws IllegalDateException {
        return new ResponseEntity<>(service.book(bookingRequest), HttpStatus.OK);
    }

}
