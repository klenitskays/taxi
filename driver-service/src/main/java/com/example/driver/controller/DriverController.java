package com.example.driver.controller;

import com.example.driver.dto.DriverDTO;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


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
    public ResponseEntity<Page<DriverDTO>> getAllDrivers(Pageable pageable) {
        Page<DriverDTO> driverPage = driverService.getAllDrivers(pageable);
        return ResponseEntity.ok(driverPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> readById(@PathVariable Long id) {
        DriverDTO driverDTO = driverService.readById(id);
        if (driverDTO != null) {
            return ResponseEntity.ok(driverDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lastName")
    public ResponseEntity<List<DriverDTO>> readByLastName(@RequestParam("lastName") String lastName) {
        List<DriverDTO> drivers = driverService.readByLastName(lastName);
        if (!drivers.isEmpty()) {
            return ResponseEntity.ok(drivers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findAvailableDrivers")
    public List<DriverDTO> getAvailableDrivers() {
        List<DriverDTO> driverDTOs = driverService.findAvailableDrivers();
        return driverDTOs;
    }

    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
}