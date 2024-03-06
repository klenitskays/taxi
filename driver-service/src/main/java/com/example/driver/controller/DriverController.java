package com.example.driver.controller;

import com.example.driver.dto.DriverDTO;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDTO> create(@RequestBody DriverDTO dto) {
        DriverDTO createdDriverDTO = driverService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDriverDTO);
    }

    @GetMapping
    public ResponseEntity<List<DriverDTO>> readAll() {
        List<DriverDTO> drivers = driverService.readAll();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DriverDTO> readById(@PathVariable Long id) {
        DriverDTO driverDTO = driverService.readById(id);
        if (driverDTO != null) {
            return ResponseEntity.ok(driverDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<DriverDTO>> readByLastName(@PathVariable String lastName) {
        List<DriverDTO> drivers = driverService.readByLastName(lastName);
        if (!drivers.isEmpty()) {
            return ResponseEntity.ok(drivers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAvailableDrivers")
    public ResponseEntity<List<DriverDTO>> getAvailableDrivers() {
        List<DriverDTO> driverDTOs = driverService.findAvailableDrivers();
        return ResponseEntity.ok(driverDTOs);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<DriverDTO> update(
            @RequestBody DriverDTO dto,
            @PathVariable Long id
    ) {
        DriverDTO updatedDriverDTO = driverService.update(dto, id);
        if (updatedDriverDTO != null) {
            return ResponseEntity.ok(updatedDriverDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}