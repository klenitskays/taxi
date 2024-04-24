package com.example.passenger.client;

import com.example.passenger.dto.PassengerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(value = "passenger-service", url = "http://localhost:8080")
public interface PassengerClient {

    @GetMapping("/passenger/available")
    List<PassengerDto> getAvailablePassenger();
    @PutMapping("/passenger/{id}/toggle-availability")
    void togglePassengerAvailability(@PathVariable("id") Long id);
}