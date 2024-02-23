package com.example.driver.impl.DriverServiceImpl;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.repo.DriverRepository;
import com.example.driver.service.DriverService;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;


    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }
    @Override
    public DriverDTO create(DriverDTO dto) {
        Driver driver = driverMapper.toDriver(dto);
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toDriverDTO(savedDriver);
    }

    @Override
    public List<DriverDTO> readAll() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO readById(Long id) {
        Driver driver = driverRepository.findById(id).orElse(null);
        if (driver != null) {
            return driverMapper.toDriverDTO(driver);
        }
        return null;
    }

    @Override
    public List<DriverDTO> readByLastName(String lastName) {
        List<Driver> drivers = driverRepository.findByLastName(lastName);
        return drivers.stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverDTO> isAvailable() {
        List<Driver> isAvailableDrivers = driverRepository.findByAvailableIsTrue();
        return isAvailableDrivers.stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO update(DriverDTO dto, Long id) {
        Driver existingDriver = driverRepository.findById(id).orElse(null);
        if (existingDriver != null) {
            driverMapper.updateDriverFromDTO(dto, existingDriver);
            Driver updatedDriver = driverRepository.save(existingDriver);
            return driverMapper.toDriverDTO(updatedDriver);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        driverRepository.deleteById(id);
    }

    @NotNull
    @Override
    public DriverDTO getDriver(DriverDTO dto, Driver driver) {
        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setContactInfo(dto.getContactInfo());
        driver.setLatitude(dto.getLatitude());
        driver.setLongitude(dto.getLongitude());
        driver.setAvailable(dto.isAvailable());
        driver.setRating(dto.getRating());
        Driver savedDriver = driverRepository.save(driver);
        return driverMapper.toDriverDTO(savedDriver);
    }
}