package com.example.passenger.controller;

import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerDTO> create(@RequestBody PassengerDTO dto) {
        PassengerDTO createdPassengerDTO = passengerService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPassengerDTO);
    }

    @GetMapping
    public ResponseEntity<Page<PassengerDTO>> getAllPassengers(Pageable pageable) {
        Page<PassengerDTO> passengerPage = passengerService.getAllPassengers(pageable);
        return ResponseEntity.ok(passengerPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> readById(@PathVariable Long id) {
        PassengerDTO passengerDTO = passengerService.readById(id);
        if (passengerDTO != null) {
            return ResponseEntity.ok(passengerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lastName")
    public ResponseEntity<List<PassengerDTO>> readByLastName(@RequestParam("lastName") String lastName) {
        List<PassengerDTO> passengers = passengerService.readByLastName(lastName);
        if (!passengers.isEmpty()) {
            return ResponseEntity.ok(passengers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTO> update(
            @RequestBody PassengerDTO dto,
            @PathVariable Long id
    ) {
        PassengerDTO updatedPassengerDTO = passengerService.update(dto, id);
        if (updatedPassengerDTO != null) {
            return ResponseEntity.ok(updatedPassengerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}