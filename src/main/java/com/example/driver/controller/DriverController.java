package com.example.driver.controller;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<Driver> create(@RequestBody DriverDTO dto) {
        Driver driver = driverService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(driver);
    }

    @GetMapping
    public ResponseEntity<List<Driver>> readAll() {
        List<Driver> drivers = driverService.readAll();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Driver> readById(@PathVariable Long id) {
        Driver driver = driverService.readById(id);
        if (driver != null) {
            return ResponseEntity.ok(driver);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<Driver>> readByLastName(@PathVariable String lastName) {
        List<Driver> drivers = driverService.readByLastName(lastName);
        if (!drivers.isEmpty()) {
            return ResponseEntity.ok(drivers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/isAvailable")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        List<Driver> isAvailableDrivers = driverService.isAvailable();
        return ResponseEntity.ok(isAvailableDrivers);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Driver> update(
            @RequestBody DriverDTO dto,
            @PathVariable Long id
    ) {
        Driver updatedDriver = driverService.update(dto, id);
        if (updatedDriver != null) {
            return ResponseEntity.ok(updatedDriver);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}