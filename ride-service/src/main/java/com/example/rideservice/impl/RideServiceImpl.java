package com.example.rideservice.impl;

import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.entity.Ride;
import com.example.rideservice.mapper.RideMapper;
import com.example.rideservice.repo.RideRepository;
import com.example.rideservice.service.RideService;
import com.example.rideservice.status.RideStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    @Override
    public RideDTO createRide(@Valid RideDTO dto) {
        Ride ride = rideMapper.toRide(dto);
        ride.setStatus(RideStatus.CREATED);
        ride.setStartTime(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);

        return rideMapper.toRideDTO(savedRide);
    }

    @Override
    public RideDTO getRideById(Integer id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        return rideOptional.map(rideMapper::toRideDTO).orElse(null);
    }

    @Override
    public RideDTO getRideByPassengerId(Integer passengerId) {
        Optional<Ride> rideOptional = rideRepository.findByPassengerId(passengerId);
        return rideOptional.map(rideMapper::toRideDTO).orElse(null);
    }

    @Override
    public RideDTO getRideByDriverId(Integer driverId) {
        Optional<Ride> rideOptional = rideRepository.findByDriverId(driverId);
        return rideOptional.map(rideMapper::toRideDTO).orElse(null);
    }

    @Override
    public RideDTO updateRide(@Valid RideDTO dto, Integer id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            rideMapper.updateRideFromDTO(dto, ride);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDTO(updatedRide);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRide(Integer id) {
        rideRepository.deleteById(id);
    }

    @Override
    public RideDTO updateRideStatus(Integer rideId, RideStatus status) {
        return null;
    }

    @Override
    public RideDTO cancelRide(Integer rideId) {
        return null;
    }

    @Override
    public RideDTO completeRide(Integer rideId) {
        return null;
    }
}