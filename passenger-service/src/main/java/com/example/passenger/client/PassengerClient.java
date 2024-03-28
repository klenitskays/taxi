package com.example.passenger.client;

import com.example.passenger.dto.PassengerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "passenger-service", url = "http://localhost:8080")
public interface PassengerClient {

    @GetMapping("/passenger")
    Page<PassengerDTO> getPassengers(@RequestParam Map<String, Object> pageable);
}
