package com.example.rideservice.repo;

import com.example.rideservice.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByPassengerIdOrderByStartTimeDesc(Long passengerId);

    List<Ride> findByDriverId(Long driverId);

    List<Ride> findNearestRidesByStartLatitudeAndStartLongitude(Double latitude, Double longitude);

}