package com.example.rideservice.controller;

import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.service.RideService;
import com.example.rideservice.status.RideStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<RideDTO>> readAll() {
        List<RideDTO> rides = rideService.readAll();
        return ResponseEntity.ok(rides);
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
    @PostMapping("/{rideId}/update-status")
    public ResponseEntity<RideDTO> updateRideStatus(@PathVariable Integer rideId, @RequestBody RideStatus status) {
        RideDTO updatedRide = rideService.updateRideStatus(rideId, status);
        if (updatedRide != null) {
            return ResponseEntity.ok(updatedRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Integer rideId) {
        RideDTO cancelledRide = rideService.cancelRide(rideId);
        if (cancelledRide != null) {
            return ResponseEntity.ok(cancelledRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rideId}/complete")
    public ResponseEntity<RideDTO> completeRide(@PathVariable Integer rideId) {
        RideDTO completedRide = rideService.completeRide(rideId);
        if (completedRide != null) {
            return ResponseEntity.ok(completedRide);
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