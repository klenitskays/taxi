package com.example.driver.service;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.repo.DriverRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver create(DriverDTO dto) {
        Driver driver = new Driver();
        driver.setId(dto.getId());
        return getDriver(dto, driver);
    }

    @NotNull
    private Driver getDriver(DriverDTO dto, Driver driver) {
        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setContactInfo(dto.getContactInfo());
        driver.setLatitude(dto.getLatitude());
        driver.setLongitude(dto.getLongitude());
        driver.setAvailable(dto.isAvailable());
        return driverRepository.save(driver);
    }

    public List<Driver> readAll() {
        return driverRepository.findAll();
    }

    public Driver readById(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    public List<Driver> readByLastName(String lastName) {
        return driverRepository.findByLastName(lastName);
    }


    public List<Driver> isAvailable() {
    return driverRepository.findByIsAvailableIsTrue();
}

    public Driver update(DriverDTO dto, Long id) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver != null) {
            return getDriver(dto, driver);
        }
        return null;
    }

    public void delete(Long id) {
        driverRepository.deleteById(id);
    }
}