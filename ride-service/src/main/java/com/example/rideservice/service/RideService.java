package com.example.rideservice.service;

import com.example.driver.dto.DriverDto;
import com.example.rideservice.dto.RideDto;
import com.example.rideservice.status.RideStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RideService {
    RideDto createRide(@Valid RideDto rideDto);
    Page<RideDto> getAllRides(Pageable pageable);

    RideDto getRideById(Integer id);

    List<RideDto> getRideByPassengerId(Integer passengerId);

    List<RideDto> getRideByDriverId(Integer driverId);

    RideDto updateRide(@Valid RideDto dto, Integer id);

    void deleteRide(Integer id);

    RideDto cancelRide(Integer rideId);

    RideDto completeRide(Integer rideId);

    RideDto acceptRide(Long rideId);
    List<DriverDto> getAvailableDrivers();
    void toggleDriverAvailability(Long driverId);
    RideDto startRide(Integer rideId);
}