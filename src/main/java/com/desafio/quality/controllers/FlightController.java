package com.desafio.quality.controllers;

import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.exceptions.IllegalDateException;
import com.desafio.quality.services.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
