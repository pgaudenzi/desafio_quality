package com.desafio.quality.repositories;

import com.desafio.quality.dtos.FlightDto;

import java.util.List;
import java.util.Set;

public interface FlightRepository {

    List<FlightDto> getAll();
    Set<String> getDestinations();
    Set<String> getOrigins();

}
