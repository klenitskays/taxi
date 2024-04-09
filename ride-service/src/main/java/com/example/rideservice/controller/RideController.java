package com.example.rideservice.controller;

import com.example.driver.client.DriverClient;
import com.example.driver.dto.DriverDTO;
import com.example.passenger.client.PassengerClient;
import com.example.passenger.dto.PassengerDTO;
import com.example.paymentservice.client.ChargeClient;
import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.service.RideService;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
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

    @PostMapping
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO dto) {
        PassengerClient passengerClient = Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder())
                .target(PassengerClient.class, "http://localhost:8080");

        List<PassengerDTO> passengers = passengerClient.getAvailablePassenger();
        if (!passengers.isEmpty()) {
            PassengerDTO passengerDTO = passengers.get(0);
            dto.setPassengerId(passengerDTO.getId());
            RideDTO createdRideDTO = rideService.createRide(dto);
            passengerClient.togglePassengerAvailability((long) passengerDTO.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdRideDTO);
        }

        return ResponseEntity.notFound().build();
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
    public ResponseEntity<RideDTO> completeRide(@PathVariable("rideId") Integer rideId) {
        RideDTO completedRide = rideService.completeRide(rideId);
        if (completedRide != null) {
            int price = completedRide.getPrice();
            ChargeRequest chargeRequest = new ChargeRequest();
            chargeRequest.setAmount((int) price);
            ChargeClient chargeClient = Feign.builder()
                    .contract(new SpringMvcContract())
                    .encoder(new JacksonEncoder())
                    .decoder(new ResponseEntityDecoder(new JacksonDecoder()))
                    .target(ChargeClient.class, "http://localhost:8084");
            Payment payment = chargeClient.charge(chargeRequest);

            DriverClient driverClient = Feign.builder()
                    .contract(new SpringMvcContract())
                    .decoder(new JacksonDecoder())
                    .target(DriverClient.class, "http://localhost:8081");

            if (completedRide.getDriverId() != null) {
                driverClient.toggleDriverAvailability(Long.valueOf(completedRide.getDriverId()));
            }

            RideDTO rideDTO = rideService.completeRide(rideId);
            if (rideDTO != null && rideDTO.getPassengerId() != null) {
                PassengerClient passengerClient = Feign.builder()
                        .contract(new SpringMvcContract())
                        .decoder(new JacksonDecoder())
                        .target(PassengerClient.class, "http://localhost:8080");

                passengerClient.togglePassengerAvailability(Long.valueOf(rideDTO.getPassengerId()));
            }

            return ResponseEntity.ok(completedRide);
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