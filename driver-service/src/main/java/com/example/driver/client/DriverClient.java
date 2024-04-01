package com.example.driver.client;

import com.example.driver.dto.DriverDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "driver-service", url = "http://localhost:8081")
public interface DriverClient {

    @GetMapping("/driver/available")
    List<DriverDTO> getAvailableDrivers();
    @PutMapping("/driver/{id}/toggle-availability")
    void toggleDriverAvailability(@PathVariable("id") Long id);
}