package com.example.rideservice.controller;

import com.example.driver.dto.DriverDTO;
import com.example.driver.service.DriverService;
import com.example.passenger.service.PassengerService;
import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.service.RideService;
import com.example.rideservice.status.RideStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;
    private final PassengerService passengerService;
    private final DriverService driverService;

    @PostMapping("/createWithDriver")
    public ResponseEntity<RideDTO> createRideWithDriver(
            @RequestParam("passengerId") Integer passengerId,
            @RequestParam("startLatitude") Double startLatitude,
            @RequestParam("startLongitude") Double startLongitude,
            @RequestParam("destinationLatitude") Double destinationLatitude,
            @RequestParam("destinationLongitude") Double destinationLongitude
    ) {
        RideDTO createdRide = rideService.createRideWithDriver(passengerId, startLatitude, startLongitude, destinationLatitude, destinationLongitude);

        if (createdRide != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRide);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Page<RideDTO>> getAllRides(Pageable pageable) {
        Page<RideDTO> ridePage = rideService.getAllRides(pageable);
        return ResponseEntity.ok(ridePage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRideById(@PathVariable Integer id) {
        RideDTO rideDTO = rideService.getRideById(id);
        if (rideDTO != null) {
            return ResponseEntity.ok(rideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/passenger")
    public ResponseEntity<List<RideDTO>> getRideByPassengerId(@RequestParam("passengerId") Integer passengerId) {
        List<RideDTO> rideDTO = rideService.getRideByPassengerId(passengerId);
        if (rideDTO != null) {
            return ResponseEntity.ok(rideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/driver")
    public ResponseEntity<List<RideDTO>> getRideByDriverId(@RequestParam("driverId") Integer driverId) {
        List<RideDTO> rideDTO = rideService.getRideByDriverId(driverId);
        if (rideDTO != null) {
            return ResponseEntity.ok(rideDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RideDTO> updateRide(@RequestBody RideDTO dto, @PathVariable Integer id) {
        RideDTO updatedRideDTO = rideService.updateRide(dto, id);
        if (updatedRideDTO != null) {
            return ResponseEntity.ok(updatedRideDTO);
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

    @PostMapping("/{rideId}/accept")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Integer rideId) {
        RideDTO acceptedRide = rideService.acceptRide(rideId);
        if (acceptedRide != null) {
            return ResponseEntity.ok(acceptedRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rideId}/start")
    public ResponseEntity<RideDTO> startRide(@PathVariable Integer rideId) {
        RideDTO inProgressRide = rideService.startRide(rideId);
        if (inProgressRide != null) {
            return ResponseEntity.ok(inProgressRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Integer id) {
        rideService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }
}