package com.example.rideservice.repo;

import com.example.rideservice.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Integer> {

    List<Ride> findByPassengerId(Integer passengerId);

    List<Ride> findByDriverId(Integer driverId);
}