package com.example.passenger.impl;


import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.entity.Passenger;
import com.example.passenger.mapper.PassengerMapper;
import com.example.passenger.repo.PassengerRepository;
import com.example.passenger.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public PassengerDTO create(@Valid PassengerDTO dto) {
        Passenger passenger = passengerMapper.toPassenger(dto);
        Passenger savedPassenger = passengerRepository.save(passenger);
        return passengerMapper.toPassengerDTO(savedPassenger);
    }
    @Override
    public List<PassengerDTO> getAllPassengers() {
        List<Passenger> passengerList = passengerRepository.findAll();
        return passengerList.stream()
                .map(passengerMapper::toPassengerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PassengerDTO readById(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            return passengerMapper.toPassengerDTO(passenger);
        }
        return null;
    }

    @Override
    public List<PassengerDTO> readByLastName(String lastName) {
        List<Passenger> passengers = passengerRepository.findByLastName(lastName);
        return passengers.stream()
                .map(passengerMapper::toPassengerDTO)
                .collect(Collectors.toList());
    }



    @Override
    public PassengerDTO update(@Valid PassengerDTO dto, Long id) {
        return passengerRepository.findById(id)
                .map(existingPassenger -> {
                    passengerMapper.updatePassengerFromDTO(dto, existingPassenger);
                    Passenger savedPassenger = passengerRepository.save(existingPassenger);
                    return passengerMapper.toPassengerDTO(savedPassenger);
                })
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public PassengerDTO getDriver(PassengerDTO dto, Passenger passenger) {
       passengerMapper.updatePassengerFromDTO(dto,passenger);
        return passengerMapper.toPassengerDTO(passenger);
    }
    @Override
    public List<PassengerDTO> findAvailablePassenger() {
        List<Passenger> availablePassengers = passengerRepository.findByAvailableIsTrue();
        return availablePassengers.stream()
                .map(passengerMapper::toPassengerDTO)
                .collect(Collectors.toList());
    }
    @Override
    public PassengerDTO toggleAvailability(Long id) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            passenger.setAvailable(!passenger.getAvailable());
            Passenger savedPassenger = passengerRepository.save(passenger);
            return passengerMapper.toPassengerDTO(savedPassenger);
        }
        return null;
    }
}
