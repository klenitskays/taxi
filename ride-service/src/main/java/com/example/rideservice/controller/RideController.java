package com.example.rideservice.controller;

import com.example.rideservice.entity.Ride;
import com.example.rideservice.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rides")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    public ResponseEntity<Ride> createRide(@RequestParam("passengerId") Integer passengerId,
                                           @RequestParam("startLatitude") Double startLatitude,
                                           @RequestParam("startLongitude") Double startLongitude,
                                           @RequestParam("destinationLatitude") Double destinationLatitude,
                                           @RequestParam("destinationLongitude") Double destinationLongitude) {
        Ride ride = rideService.createRide(passengerId, startLatitude, startLongitude, destinationLatitude, destinationLongitude);
        return ResponseEntity.status(HttpStatus.CREATED).body(ride);
    }

    @PostMapping("/{rideId}/approve")
    public ResponseEntity<String> approveRide(@PathVariable("rideId") Long rideId) {
        rideService.approveRide(rideId);
        return ResponseEntity.ok("Ride approved");
    }

    @PostMapping("/{rideId}/start")
    public ResponseEntity<String> startRide(@PathVariable("rideId") Long rideId) {
        rideService.startRide(rideId);
        return ResponseEntity.ok("Ride started");
    }

    @PostMapping("/{rideId}/complete")
    public ResponseEntity<String> completeRide(@PathVariable("rideId") Long rideId) {
        rideService.completeRide(rideId);
        return ResponseEntity.ok("Ride completed");
    }

    @GetMapping("/{passengerId}/history")
    public ResponseEntity<List<Ride>> getRideHistoryForPassenger(@PathVariable("passengerId") Long passengerId) {
        List<Ride> rideHistory = rideService.getRideHistoryForPassenger(passengerId);
        return ResponseEntity.ok(rideHistory);
    }

    @GetMapping("/nearest")
    public ResponseEntity<List<Ride>> findNearestRides(@RequestParam("latitude") Double latitude,
                                                       @RequestParam("longitude") Double longitude) {
        List<Ride> nearestRides = rideService.findNearestRidesByLocation(latitude, longitude);
        return ResponseEntity.ok(nearestRides);
    }
}
