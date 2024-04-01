package com.example.passenger.service;

import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.entity.Passenger;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface PassengerService {
    PassengerDTO create(@Valid PassengerDTO dto);
    Page<PassengerDTO> getAllPassengers(Pageable pageable);
    PassengerDTO readById(Long id);
    List<PassengerDTO> readByLastName(String lastName);
    PassengerDTO update(@Valid PassengerDTO dto, Long id);
    void delete(Long id);
    @NotNull
    PassengerDTO getDriver(@Valid PassengerDTO dto, Passenger passenger);
    List<PassengerDTO> findAvailablePassenger();
    PassengerDTO toggleAvailability(Long id);

}