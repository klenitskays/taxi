package com.example.driver.controller;

import com.example.driver.entity.Driver;
import com.example.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
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
        public List<Driver> getAllDrivers() {
            return driverService.getAllDrivers();
        }

        @GetMapping("/{id}")
        public Optional<Driver> getDriverById(@PathVariable int id) {
            return driverService.getDriverById(id);
        }

        @PostMapping
        public Driver saveDriver(@RequestBody Driver driver) {
            return driverService.saveDriver(driver);
        }

        @PutMapping("/{id}")
        public void updateDriver(@PathVariable int id, @RequestBody Driver driver) {
            driver.setId(id);
            driverService.updateDriver(driver);
        }

        @DeleteMapping("/{id}")
        public void deleteDriverById(@PathVariable int id) {
            driverService.deleteDriverById(id);
        }

        @GetMapping("/byFirstName/{firstName}")
        public List<Driver> getDriversByFirstName(@PathVariable String firstName) {
            return driverService.getDriversByFirstName(firstName);
        }

        @GetMapping("/byLastName/{lastName}")
        public List<Driver> getDriversByLastName(@PathVariable String lastName) {
            return driverService.getDriversByLastName(lastName);
        }

        @GetMapping("/byFullName/{firstName}/{lastName}")
        public List<Driver> getDriversByFullName(@PathVariable String firstName, @PathVariable String lastName) {
            return driverService.getDriversByFullName(firstName, lastName);
        }

    }