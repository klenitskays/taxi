package com.example.rideservice.service;

import com.example.rideservice.entity.Ride;
import com.example.rideservice.repo.RideRepository;
import com.example.rideservice.status.RideStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;

    @Autowired
    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Transactional
    public Ride createRide(Integer passengerId, Double startLatitude, Double startLongitude,
                           Double destinationLatitude, Double destinationLongitude) {
        Ride ride = new Ride();
        ride.setPassengerId(passengerId);
        ride.setStartLatitude(startLatitude);
        ride.setStartLongitude(startLongitude);
        ride.setDestinationLatitude(destinationLatitude);
        ride.setDestinationLongitude(destinationLongitude);
        ride.setStatus(RideStatus.CREATED);
        ride.setStartTime(LocalDateTime.now());

        return rideRepository.save(ride);
    }

    @Transactional
    public void approveRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.ACCEPTED);
        rideRepository.save(ride);
    }

    @Transactional
    public void startRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.IN_PROGRESS);
        rideRepository.save(ride);
    }

    @Transactional
    public void completeRide(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.COMPLETED);
        ride.setEndTime(LocalDateTime.now());
        rideRepository.save(ride);
    }

    public List<Ride> getRideHistoryForPassenger(Long passengerId) {
        return rideRepository.findByPassengerIdOrderByStartTimeDesc(passengerId);
    }

    public List<Ride> findNearestRidesByLocation(Double latitude, Double longitude) {
        return rideRepository.findNearestRidesByStartLatitudeAndStartLongitude(latitude, longitude);
    }
}