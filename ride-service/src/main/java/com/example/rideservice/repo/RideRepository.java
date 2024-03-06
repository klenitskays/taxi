package com.example.rideservice.repo;

import com.example.rideservice.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {

    Optional<Ride> findByPassengerId(Integer passengerId);

    Optional<Ride> findByDriverId(Integer driverId);
}