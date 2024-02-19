package com.example.driver.service;

import com.example.driver.Driver;
import com.example.driver.repo.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(int id) {
        return driverRepository.findById(id);
    }

    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public void updateDriver(Driver driver) {
        driverRepository.save(driver);
    }

    public void deleteDriverById(int id) {
        driverRepository.deleteById(id);
    }

    public List<Driver> getDriversByFirstName(String firstName) {
        return driverRepository.findByFirstName(firstName);
    }

    public List<Driver> getDriversByLastName(String lastName) {
        return driverRepository.findByLastName(lastName);
    }

    public List<Driver> getDriversByFullName(String firstName, String lastName) {
        return driverRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}
