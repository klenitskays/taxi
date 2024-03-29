package com.example.passenger.client;

import com.example.passenger.dto.PassengerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "passenger-service", url = "http://localhost:8080")
public interface PassengerClient {

    @GetMapping("/passenger")
    List<PassengerDTO> getPassengers(@RequestParam int pageNumber, @RequestParam int pageSize);
}