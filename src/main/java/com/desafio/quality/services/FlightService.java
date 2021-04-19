package com.desafio.quality.services;

import com.desafio.quality.dtos.FlightDto;
import com.desafio.quality.exceptions.IllegalDateException;

import java.util.List;

public interface FlightService {

    List<FlightDto> getFlights(String dateFrom, String dateTo, String origin, String destination) throws IllegalDateException;


}
