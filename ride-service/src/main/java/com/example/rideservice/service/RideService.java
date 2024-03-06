package com.example.rideservice.service;

import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.status.RideStatus;
import jakarta.validation.Valid;

public interface RideService {

    RideDTO createRide(@Valid RideDTO rideDTO);

    RideDTO getRideById(Integer id);

    RideDTO getRideByPassengerId(Integer passengerId);

    RideDTO getRideByDriverId(Integer driverId);

    RideDTO updateRide(@Valid RideDTO dto, Integer id);

    void deleteRide(Integer id);

    RideDTO updateRideStatus(Integer rideId, RideStatus status);

    RideDTO cancelRide(Integer rideId);

    RideDTO completeRide(Integer rideId);
}