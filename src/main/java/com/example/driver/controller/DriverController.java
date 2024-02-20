package com.example.driver.controller;

import com.example.driver.entity.Driver;
import com.example.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable int id) {
        Optional<Driver> driver = driverService.getDriverById(id);
        return driver.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Driver> saveDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverService.saveDriver(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDriver);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDriver(@PathVariable int id, @RequestBody Driver driver) {
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
    public ResponseEntity<List<Driver>> getDriversByFirstName(@PathVariable String firstName) {
        List<Driver> drivers = driverService.getDriversByFirstName(firstName);
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/byLastName/{lastName}")
    public ResponseEntity<List<Driver>> getDriversByLastName(@PathVariable String lastName) {
        List<Driver> drivers = driverService.getDriversByLastName(lastName);
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/byFullName/{firstName}/{lastName}")
    public ResponseEntity<List<Driver>> getDriversByFullName(@PathVariable String firstName, @PathVariable String lastName) {
        List<Driver> drivers = driverService.getDriversByFullName(firstName, lastName);
        return ResponseEntity.ok(drivers);
    }

}