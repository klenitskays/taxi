package com.example.driver.service;

import com.example.driver.dto.DriverDto;
import com.example.driver.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface DriverService {
    DriverDto create(@Valid DriverDto dto);
    Page<DriverDto> getAllDrivers(Pageable pageable);
    DriverDto readById(Long id);
    List<DriverDto> readByLastName(String lastName);
    List<DriverDto> findAvailableDrivers();
    DriverDto update(@Valid DriverDto dto, Long id);
    void delete(Long id);
    @NotNull
    DriverDto getDriver(@Valid DriverDto dto, Driver driver);
    DriverDto toggleAvailability(Long id);
}