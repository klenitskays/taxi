package com.example.driver.impl;

import com.example.driver.dto.DriverDto;
import com.example.driver.entity.Driver;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.repo.DriverRepository;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public DriverDto create(DriverDto dto) {
        Driver driver = driverMapper.toDriver(dto);
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toDriverDto(savedDriver);
    }

    @Override
    public Page<DriverDto> getAllDrivers(Pageable pageable) {
        Page<Driver> driverPage = driverRepository.findAll(pageable);
        return driverPage.map(driverMapper::toDriverDto);
    }

    @Override
    public DriverDto readById(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) {
            Driver driver = optionalDriver.get();
            return driverMapper.toDriverDto(driver);
        }
        return null;
    }

    @Override
    public List<DriverDto> readByLastName(String lastName) {
        List<Driver> drivers = driverRepository.findByLastName(lastName);
        return drivers.stream()
                .map(driverMapper::toDriverDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverDto> findAvailableDrivers() {
        List<Driver> availableDrivers = driverRepository.findByAvailableIsTrue();
        return availableDrivers.stream()
                .map(driverMapper::toDriverDto)
                .collect(Collectors.toList());
    }



    @Override
    public DriverDto update(DriverDto dto, Long id) {
        return driverRepository.findById(id)
                .map(existingDriver -> {
                    driverMapper.updateDriverFromDto(dto, existingDriver);
                    Driver savedDriver = driverRepository.save(existingDriver);
                    return driverMapper.toDriverDto(savedDriver);
                })
                .orElse(null);
    }
    @Override
    public void delete(Long id) {
        driverRepository.deleteById(id);
    }

    @NotNull
    @Override
    public DriverDto getDriver(DriverDto dto, Driver driver) {
        driverMapper.updateDriverFromDto(dto, driver);
        return driverMapper.toDriverDto(driver);
    }
    @Override
    public DriverDto toggleAvailability(Long id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) {
            Driver driver = optionalDriver.get();
            driver.setAvailable(!driver.getAvailable());
            Driver savedDriver = driverRepository.save(driver);
            return driverMapper.toDriverDto(savedDriver);
        }
        return null;
    }
}