package com.example.driver.impl.DriverServiceImpl;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.mapper.DriverMapper;
import com.example.driver.repo.DriverRepository;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
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
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isPresent()) {
            Driver driver = optionalDriver.get();
            return driverMapper.toDriverDTO(driver);
        }
        return null;
    }

    @Override
    public List<DriverDTO> readByLastName(String lastName) {
        Optional<Driver> drivers = driverRepository.findByLastName(lastName);
        return drivers.stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriverDTO> findAvailableDrivers() {
        Optional<Driver> isAvailableDrivers = driverRepository.findByAvailableIsTrue();
        return isAvailableDrivers.stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }




    @Override
    public DriverDTO update(DriverDTO dto, Long id) {
        return driverRepository.findById(id)
                .map(existingDriver -> {
                    driverMapper.updateDriverFromDTO(dto, existingDriver);
                    Driver savedDriver = driverRepository.save(existingDriver);
                    return driverMapper.toDriverDTO(savedDriver);
                })
                .orElse(null);
    }
    @Override
    public void delete(Long id) {
        driverRepository.deleteById(id);
    }

    @NotNull
    @Override
    public DriverDTO getDriver(DriverDTO dto, Driver driver) {
        driverMapper.updateDriverFromDTO(dto, driver);
        return driverMapper.toDriverDTO(driver);
    }
}