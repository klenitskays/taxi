package com.example.driver.service;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface DriverService {
    DriverDTO create(@Valid DriverDTO dto);
    Page<DriverDTO> getAllDrivers(Pageable pageable);
    DriverDTO readById(Long id);
    List<DriverDTO> readByLastName(String lastName);
    List<DriverDTO> findAvailableDrivers();
    DriverDTO update(@Valid DriverDTO dto, Long id);
    void delete(Long id);
    @NotNull
    DriverDTO getDriver(@Valid DriverDTO dto, Driver driver);
    DriverDTO toggleAvailability(Long id);
}