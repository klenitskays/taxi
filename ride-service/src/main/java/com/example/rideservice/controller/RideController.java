package com.example.rideservice.controller;

import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO dto) {
        RideDTO createdRideDTO = rideService.createRide(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRideDTO);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RideDTO> getRideById(@PathVariable Integer id) {
        RideDTO rideDTO = rideService.getRideById(id);
        if (rideDTO != null) {
            return ResponseEntity.ok(rideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<RideDTO> getRideByPassengerId(@PathVariable Integer passengerId) {
        RideDTO rideDTO = rideService.getRideByPassengerId(passengerId);
        if (rideDTO != null) {
            return ResponseEntity.ok(rideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<RideDTO> getRideByDriverId(@PathVariable Integer driverId) {
        RideDTO rideDTO = rideService.getRideByDriverId(driverId);
        if (rideDTO != null) {
            return ResponseEntity.ok(rideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<RideDTO> updateRide(@RequestBody RideDTO dto, @PathVariable Integer id) {
        RideDTO updatedRideDTO = rideService.updateRide(dto, id);
        if (updatedRideDTO != null) {
            return ResponseEntity.ok(updatedRideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Integer id) {
        rideService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }
}