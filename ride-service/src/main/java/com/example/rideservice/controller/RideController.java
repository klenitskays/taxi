package com.example.rideservice.controller;

import com.example.driver.client.DriverClient;
import com.example.driver.dto.DriverDto;
import com.example.passenger.client.PassengerClient;
import com.example.passenger.dto.PassengerDto;
import com.example.paymentservice.client.ChargeClient;
import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import com.example.rideservice.dto.RideDto;
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
    public ResponseEntity<RideDto> createRide(@RequestBody RideDto dto) {
        PassengerClient passengerClient = Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder())
                .target(PassengerClient.class, "http://localhost:8080");

        List<PassengerDto> passengers = passengerClient.getAvailablePassenger();
        if (!passengers.isEmpty()) {
            PassengerDto passengerDto = passengers.get(0);
            dto.setPassengerId(passengerDto.getId());
            RideDto createdRideDto = rideService.createRide(dto);
            passengerClient.togglePassengerAvailability((long) passengerDto.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(createdRideDto);
        }

        return ResponseEntity.notFound().build();
    }
    @GetMapping
    public ResponseEntity<Page<RideDto>> getAllRides(Pageable pageable) {
        Page<RideDto> ridePage = rideService.getAllRides(pageable);
        return ResponseEntity.ok(ridePage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RideDto> getRideById(@PathVariable Integer id) {
        RideDto rideDto = rideService.getRideById(id);
        if (rideDto != null) {
            return ResponseEntity.ok(rideDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/passenger")
    public ResponseEntity<List<RideDto>> getRideByPassengerId(@RequestParam("passengerId") Integer passengerId) {
        List<RideDto> rideDto = rideService.getRideByPassengerId(passengerId);
        if (rideDto != null) {
            return ResponseEntity.ok(rideDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/driver")
    public ResponseEntity<List<RideDto>> getRideByDriverId(@RequestParam("driverId") Integer driverId) {
        List<RideDto> rideDto = rideService.getRideByDriverId(driverId);
        if (rideDto != null) {
            return ResponseEntity.ok(rideDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{rideId}/accept")
    public ResponseEntity<RideDto> acceptRide(@PathVariable("rideId") Integer rideId) {
        RideDto updatedRideDto = rideService.acceptRide(Long.valueOf(rideId));
        if (updatedRideDto != null && updatedRideDto.getDriverId() != null && updatedRideDto.getDriverId() == 0) {
            List<DriverDto> availableDrivers = rideService.getAvailableDrivers();
            if (!availableDrivers.isEmpty()) {
                DriverDto driverDto = availableDrivers.get(0);
                rideService.toggleDriverAvailability((long) driverDto.getId());

                updatedRideDto.setDriverId(driverDto.getId());
                updatedRideDto = rideService.updateRide(updatedRideDto, rideId);
                return ResponseEntity.ok(updatedRideDto);
            }
        }

        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<RideDto> updateRide(@RequestBody RideDto dto, @PathVariable Integer id) {
        RideDto updatedRideDto = rideService.updateRide(dto, id);
        if (updatedRideDto != null) {
            return ResponseEntity.ok(updatedRideDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Integer rideId) {
        RideDto cancelledRide = rideService.cancelRide(rideId);
        if (cancelledRide != null) {
            return ResponseEntity.ok(cancelledRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{rideId}/complete")
    public ResponseEntity<RideDto> completeRide(@PathVariable("rideId") Integer rideId) {
        RideDto completedRide = rideService.completeRide(rideId);
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

            RideDto rideDto = rideService.completeRide(rideId);
            if (rideDto != null && rideDto.getPassengerId() != null) {
                PassengerClient passengerClient = Feign.builder()
                        .contract(new SpringMvcContract())
                        .decoder(new JacksonDecoder())
                        .target(PassengerClient.class, "http://localhost:8080");

                passengerClient.togglePassengerAvailability(Long.valueOf(rideDto.getPassengerId()));
            }

            return ResponseEntity.ok(completedRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{rideId}/start")
    public ResponseEntity<RideDto> startRide(@PathVariable Integer rideId) {
        RideDto inProgressRide = rideService.startRide(rideId);
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