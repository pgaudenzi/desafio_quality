package com.desafio.quality.controllers;

import com.desafio.quality.dtos.*;
import com.desafio.quality.exceptions.AvailabilityException;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.services.FlightService;
import com.desafio.quality.utils.ValidationsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FlightController {

    private final FlightService service;

    public FlightController(FlightService service) {
        this.service = service;
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightDto>> getFlights(@RequestParam(required = false) String dateFrom,
                                                      @RequestParam(required = false) String dateTo,
                                                      @RequestParam(required = false) String origin,
                                                      @RequestParam(required = false) String destination) throws IllegalDateException {
        return new ResponseEntity<>(service.getFlights(dateFrom, dateTo, origin, destination), HttpStatus.OK);
    }

    @PostMapping("/flight-reservation")
    public ResponseEntity<BookingResponseDto<FlightReservationDto>> bookFlight(
            @RequestBody BookingRequestDto<FlightReservationDto> request) throws IllegalDateException, AvailabilityException {
        //Validate request params
        ValidationsUtil.validateMailFormat(request.getUserName());
        ValidationsUtil.validateDates(request.getBooking().getDateFrom(), request.getBooking().getDateTo());
        ValidationsUtil.validatePeopleAmount(request.getBooking().getSeats(), request.getBooking().getPeople().size());

        return new ResponseEntity<>(service.book(request), HttpStatus.OK);
    }
}
