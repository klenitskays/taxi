package com.example.driver.impl.DriverServiceImpl;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.repo.DriverRepository;
import com.example.driver.service.DriverService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver create(DriverDTO dto) {
        Driver driver = new Driver();
        driver.setId(dto.getId());
        return getDriver(dto, driver, driverRepository);
    }

    @Override
    public List<Driver> readAll() {
        return driverRepository.findAll();
    }

    @Override
    public Driver readById(Long id) {
        return driverRepository.findById(id).orElse(null);
    }

    @Override
    public List<Driver> readByLastName(String lastName) {
        return driverRepository.findByLastName(lastName);
    }

    @Override
    public List<Driver> isAvailable() {
        return driverRepository.findByIsAvailableIsTrue();
    }

    @Override
    public Driver update(DriverDTO dto, Long id) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver != null) {
            return getDriver(dto, driver, driverRepository);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        driverRepository.deleteById(id);
    }

    @NotNull
    @Override
    public Driver getDriver(DriverDTO dto, Driver driver, DriverRepository driverRepository) {
        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setContactInfo(dto.getContactInfo());
        driver.setLatitude(dto.getLatitude());
        driver.setLongitude(dto.getLongitude());
        driver.setAvailable(dto.isAvailable());
        return driverRepository.save(driver);
    }
}