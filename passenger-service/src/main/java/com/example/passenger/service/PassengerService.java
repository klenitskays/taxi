package com.example.passenger.service;

import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.entity.Passenger;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.NotNull;


import java.util.List;

public interface PassengerService {
    PassengerDTO create(@Valid PassengerDTO dto);
    List<PassengerDTO> readAll();
    PassengerDTO readById(Long id);
    List<PassengerDTO> readByLastName(String lastName);
    PassengerDTO update(@Valid PassengerDTO dto, Long id);
    void delete(Long id);
    @NotNull
    PassengerDTO getDriver(@Valid PassengerDTO dto, Passenger passenger);
}