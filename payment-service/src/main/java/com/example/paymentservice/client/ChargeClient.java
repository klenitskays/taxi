package com.example.paymentservice.client;

import com.example.paymentservice.entity.ChargeRequest;
import com.example.paymentservice.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(value = "payment-service", url = "http://localhost:8084")
public interface ChargeClient {
    @PostMapping(value = "/charge", consumes = "application/json", produces = "application/json")
    Payment charge(@RequestBody ChargeRequest chargeRequest);
}