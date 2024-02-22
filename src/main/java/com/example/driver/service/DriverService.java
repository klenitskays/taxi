package com.example.driver.service;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.repo.DriverRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface DriverService {

    Driver create(DriverDTO dto);

    List<Driver> readAll();

    Driver readById(Long id);

    List<Driver> readByLastName(String lastName);

    List<Driver> isAvailable();

    Driver update(DriverDTO dto, Long id);

    void delete(Long id);

    @NotNull
    Driver getDriver(DriverDTO dto, Driver driver, DriverRepository driverRepository);
}