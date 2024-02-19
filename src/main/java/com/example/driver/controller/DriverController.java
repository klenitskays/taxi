package com.example.driver.controller;

import com.example.driver.Driver;
import com.example.driver.repo.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private DriverRepository driverRepository;

    public DriverController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public Driver registerDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverRepository.save(driver);
        kafkaTemplate.send("driver-registration", "Driver registered: " + savedDriver.getId());
        return savedDriver;
    }

    // Другие методы для работы с водителями (получение информации, обновление местоположения, отмена заказа и т.д.)
}