package com.example.driver.controller;

import com.example.driver.dto.DriverDto;
import com.example.driver.dto.DriverListDto;
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
    public ResponseEntity<DriverDto> create(@RequestBody DriverDto dto) {
        DriverDto createdDriverDto = driverService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDriverDto);
    }

    @GetMapping
    public ResponseEntity<Page<DriverDto>> getAllDrivers(Pageable pageable) {
        Page<DriverDto> driverPage = driverService.getAllDrivers(pageable);
        return ResponseEntity.ok(driverPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> readById(@PathVariable Long id) {
        DriverDto driverDto = driverService.readById(id);
        if (driverDto != null) {
            return ResponseEntity.ok(driverDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lastName")
    public ResponseEntity<DriverListDto> readByLastName(@RequestParam("lastName") String lastName) {
        List<DriverDto> drivers = driverService.readByLastName(lastName);
        DriverListDto driverListDto = new DriverListDto(drivers);
        return ResponseEntity.ok(driverListDto);
    }

    @GetMapping("/available")
    public List<DriverDto> getAvailableDrivers() {
        List<DriverDto> driverDtos = driverService.findAvailableDrivers();
        return driverDtos;
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverDto> update(
            @RequestBody DriverDto dto,
            @PathVariable Long id
    ) {
        DriverDto updatedDriverDto = driverService.update(dto, id);
        if (updatedDriverDto != null) {
            return ResponseEntity.ok(updatedDriverDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        driverService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/toggle-availability")
    public ResponseEntity<DriverDto> toggleDriverAvailability(@PathVariable("id") Long id) {
        DriverDto updatedDriverDto = driverService.toggleAvailability(id);
        if (updatedDriverDto != null) {
            return ResponseEntity.ok(updatedDriverDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}