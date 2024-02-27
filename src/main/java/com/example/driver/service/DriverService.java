package com.example.driver.service;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface DriverService {
    DriverDTO create(@Valid DriverDTO dto);
    List<DriverDTO> readAll();
    DriverDTO readById(Long id);
    List<DriverDTO> readByLastName(String lastName);
    List<DriverDTO> findAvailableDrivers();
    DriverDTO update(@Valid DriverDTO dto, Long id);
    void delete(Long id);
    @NotNull
    DriverDTO getDriver(@Valid DriverDTO dto, Driver driver);
}