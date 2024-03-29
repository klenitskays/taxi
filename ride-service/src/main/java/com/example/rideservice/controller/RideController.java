package com.example.rideservice.controller;

import com.example.driver.dto.DriverDTO;
import com.example.passenger.client.PassengerClient;
import com.example.passenger.dto.PassengerDTO;
import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.service.RideService;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<RideDTO> createRide(@RequestBody RideDTO dto) {
        PassengerClient passengerClient = Feign.builder()
                .decoder(new JacksonDecoder())
                .target(PassengerClient.class, "http://localhost:8080");

        Pageable pageable = PageRequest.of(0, 10);
        Page<PassengerDTO> passengerPage = passengerClient.getPassengers((Map<String, Object>) pageable);

        List<PassengerDTO> passengers = passengerPage.getContent();
        if (!passengers.isEmpty()) {
            PassengerDTO passengerDTO = passengers.get(0);
            dto.setPassengerId(passengerDTO.getId());
            RideDTO createdRideDTO = rideService.createRide(dto);
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
    public ResponseEntity<RideDTO> completeRide(@PathVariable Integer rideId) {
        RideDTO completedRide = rideService.completeRide(rideId);
        if (completedRide != null) {
            return ResponseEntity.ok(completedRide);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

  /*  @PostMapping("/{rideId}/accept")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Integer rideId) {
        String driverUrl = "http://localhost:8081/driver/available"; // URL эндпоинта для получения списка доступных водителей

        ResponseEntity<List<DriverDTO>> response = restTemplate.exchange(
                driverUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DriverDTO>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<DriverDTO> availableDrivers = response.getBody();

            if (!availableDrivers.isEmpty()) {
                DriverDTO selectedDriver = availableDrivers.get(0); // Выбираем первого водителя из списка
                RideDTO acceptedRide = rideService.acceptRide(rideId, selectedDriver.getId()); // Принимаем поездку с выбранным водителем

                if (acceptedRide != null) {
                    return ResponseEntity.ok(acceptedRide);
                }
            }
        }

        return ResponseEntity.notFound().build();
    }*/

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