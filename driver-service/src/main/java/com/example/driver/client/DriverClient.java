package com.example.driver.client;

import com.example.driver.dto.DriverDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "driver-service", url = "http://localhost:8081")
public interface DriverClient {

    @GetMapping("/driver/available")
    List<DriverDTO> getAvailableDrivers();

}