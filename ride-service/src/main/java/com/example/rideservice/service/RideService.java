package com.example.rideservice.service;

import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.status.RideStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RideService {
    RideDTO createRide(@Valid RideDTO rideDTO);
    Page<RideDTO> getAllRides(Pageable pageable);

    RideDTO getRideById(Integer id);

    List<RideDTO> getRideByPassengerId(Integer passengerId);

    List<RideDTO> getRideByDriverId(Integer driverId);

    RideDTO updateRide(@Valid RideDTO dto, Integer id);

    void deleteRide(Integer id);

    RideDTO cancelRide(Integer rideId);

    RideDTO completeRide(Integer rideId);

    RideDTO acceptRide(Integer rideId);

    RideDTO startRide(Integer rideId);
}