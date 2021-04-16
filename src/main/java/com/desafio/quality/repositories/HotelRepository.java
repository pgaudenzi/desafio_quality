package com.desafio.quality.repositories;

import com.desafio.quality.dtos.HotelDto;

import java.util.List;
import java.util.Set;

public interface HotelRepository {

    List<HotelDto> getAll();
    Set<String> getLocations();

}
