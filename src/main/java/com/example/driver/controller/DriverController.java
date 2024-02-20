package com.example.driver.controller;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.service.DriverService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Builder
@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getAllDrivers() {
        List<DriverDTO> driverDTOs = driverService.getAllDrivers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(driverDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable int id) {
        Optional<DriverDTO> driver = driverService.getDriverById(id);
        return driver.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DriverDTO> saveDriver(@RequestBody DriverDTO driverDTO) {
        Driver driver = convertToEntity(driverDTO);
        Driver savedDriver = driverService.saveDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedDriver));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDriver(@PathVariable int id, @RequestBody DriverDTO driverDTO) {
        Driver driver = convertToEntity(driverDTO);
        driver.setId(id);
        driverService.updateDriver(driver);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriverById(@PathVariable int id) {
        driverService.deleteDriverById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byFirstName/{firstName}")
    public ResponseEntity<List<DriverDTO>> getDriversByFirstName(@PathVariable String firstName) {
        List<DriverDTO> driverDTOs = driverService.getDriversByFirstName(firstName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(driverDTOs);
    }

    @GetMapping("/byLastName/{lastName}")
    public ResponseEntity<List<DriverDTO>> getDriversByLastName(@PathVariable String lastName) {
        List<DriverDTO> driverDTOs = driverService.getDriversByLastName(lastName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(driverDTOs);
    }

    @GetMapping("/byFullName/{firstName}/{lastName}")
    public ResponseEntity<List<DriverDTO>> getDriversByFullName(@PathVariable String firstName, @PathVariable String lastName) {
        List<DriverDTO> driverDTOs = driverService.getDriversByFullName(firstName, lastName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(driverDTOs);
    }

    private DriverDTO convertToDTO(Driver driver) {
        return DriverDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .contactInfo(driver.getContactInfo())
                .latitude(driver.getLatitude())
                .longitude(driver.getLongitude())
                .available(driver.isAvailable())
                .build();
    }

    private Driver convertToEntity(DriverDTO driverDTO) {
        return Driver.builder()
                .id(driverDTO.getId())
                .firstName(driverDTO.getFirstName())
                .lastName(driverDTO.getLastName())
                .contactInfo(driverDTO.getContactInfo())
                .latitude(driverDTO.getLatitude())
                .longitude(driverDTO.getLongitude())
                .available(driverDTO.isAvailable())
                .build();
    }
}