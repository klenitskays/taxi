package com.example.passenger.impl;


import com.example.passenger.dto.PassengerDto;
import com.example.passenger.entity.Passenger;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.repo.PassengerRepository;
import com.example.passenger.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerDto create(@Valid PassengerDto dto) {
        Passenger passenger = passengerMapper.toPassenger(dto);
        Passenger savedPassenger = passengerRepository.save(passenger);
        return passengerMapper.toPassengerDto(savedPassenger);
    }
    @Override
    public List<PassengerDto> getAllPassengers() {
        List<Passenger> passengerList = passengerRepository.findAll();
        return passengerList.stream()
                .map(passengerMapper::toPassengerDto)
                .collect(Collectors.toList());
    }

    @Override
    public PassengerDto readById(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            return passengerMapper.toPassengerDto(passenger);
        }
        return null;
    }

    @Override
    public List<PassengerDto> readByLastName(String lastName) {
        List<Passenger> passengers = passengerRepository.findByLastName(lastName);
        return passengers.stream()
                .map(passengerMapper::toPassengerDto)
                .collect(Collectors.toList());
    }



    @Override
    public PassengerDto update(@Valid PassengerDto dto, Long id) {
        return passengerRepository.findById(id)
                .map(existingPassenger -> {
                    passengerMapper.updatePassengerFromDTO(dto, existingPassenger);
                    Passenger savedPassenger = passengerRepository.save(existingPassenger);
                    return passengerMapper.toPassengerDto(savedPassenger);
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public PassengerDto getDriver(PassengerDto dto, Passenger passenger) {
       passengerMapper.updatePassengerFromDTO(dto,passenger);
        return passengerMapper.toPassengerDto(passenger);
    }
    @Override
    public List<PassengerDto> findAvailablePassenger() {
        List<Passenger> availablePassengers = passengerRepository.findByAvailableIsTrue();
        return availablePassengers.stream()
                .map(passengerMapper::toPassengerDto)
                .collect(Collectors.toList());
    }
    @Override
    public PassengerDto toggleAvailability(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            passenger.setAvailable(!passenger.getAvailable());
            Passenger savedPassenger = passengerRepository.save(passenger);
            return passengerMapper.toPassengerDto(savedPassenger);
        }
        return null;
    }
}
