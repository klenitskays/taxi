package com.example.passenger.client;

import com.example.passenger.dto.PassengerDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "passenger-service", url = "http://localhost:8080")
public interface PassengerClient {

    @GetMapping("/passenger")
    Page<PassengerDTO> getPassengers(@Qualifier("pageable") Pageable pageable);
}