package com.example.passenger.service;

import com.example.passenger.dto.PassengerDto;
import com.example.passenger.entity.Passenger;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public interface PassengerService {
    PassengerDto create(@Valid PassengerDto dto);
    List<PassengerDto> getAllPassengers();
    PassengerDto readById(Long id);
    List<PassengerDto> readByLastName(String lastName);
    PassengerDto update(@Valid PassengerDto dto, Long id);
    void delete(Long id);
    @NotNull
    PassengerDto getDriver(@Valid PassengerDto dto, Passenger passenger);
    List<PassengerDto> findAvailablePassenger();
    PassengerDto toggleAvailability(Long id);

}