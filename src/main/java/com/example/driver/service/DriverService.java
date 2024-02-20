package com.example.driver.service;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.repo.DriverRepository;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Entity
@Setter
@Getter
@Builder
@Service
@AllArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<DriverDTO> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DriverDTO> getDriverById(int id) {
        return driverRepository.findById(id)
                .map(this::convertToDTO);
    }

    public DriverDTO saveDriver(DriverDTO driverDTO) {
        Driver driver = convertToEntity(driverDTO);
        return convertToDTO(driverRepository.save(driver));
    }

    public void updateDriver(DriverDTO driverDTO) {
        Driver driver = convertToEntity(driverDTO);
        driverRepository.save(driver);
    }

    public void deleteDriverById(int id) {
        driverRepository.deleteById(id);
    }

    public List<DriverDTO> getDriversByFirstName(String firstName) {
        List<Driver> drivers = driverRepository.findByFirstName(firstName);
        return drivers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DriverDTO> getDriversByLastName(String lastName) {
        List<Driver> drivers = driverRepository.findByLastName(lastName);
        return drivers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DriverDTO> getDriversByFullName(String firstName, String lastName) {
        List<Driver> drivers = driverRepository.findByFirstNameAndLastName(firstName, lastName);
        return drivers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DriverDTO convertToDTO(Driver driver) {
        return DriverDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .contactInfo(driver.getContactInfo())
                .latitude(driver.getLatitude())
                .longitude(driver.getLongitude())
                .available(driver.isAvailable())
                .build();
    }

    private Driver convertToEntity(DriverDTO driverDTO) {
        return Driver.builder()
                .id(driverDTO.getId())
                .firstName(driverDTO.getFirstName())
                .lastName(driverDTO.getLastName())
                .contactInfo(driverDTO.getContactInfo())
                .latitude(driverDTO.getLatitude())
                .longitude(driverDTO.getLongitude())
                .available(driverDTO.isAvailable())
                .build();
    }
}