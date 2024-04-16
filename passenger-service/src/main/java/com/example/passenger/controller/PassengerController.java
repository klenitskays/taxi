package com.example.passenger.controller;

import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
import com.example.passenger.service.PassengerService;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    private final PassengerService passengerService;
    private final KafkaProducer<String, String> producer;
    private final String topicName;

    @Autowired
    public PassengerController(PassengerService passengerService, KafkaProducer<String, String> producer, String topicName) {
        this.passengerService = passengerService;
        this.producer = producer;
        this.topicName = topicName;
    }

    @PostMapping
    public ResponseEntity<PassengerDto> create(@RequestBody PassengerDto dto) {
        PassengerDto createdPassengerDTO = passengerService.create(dto);

        String message = "Новый пассажир создан: " + createdPassengerDTO.getFirstName() + " " + createdPassengerDTO.getLastName();
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("Сообщение успешно отправлено в Kafka. Topic: " + metadata.topic() +
                        ", Partition: " + metadata.partition() + ", Offset: " + metadata.offset());
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPassengerDTO);
    }


    @GetMapping
    public ResponseEntity<PassengerDtoList> getAllPassengers() {
        List<PassengerDto> passengerList = passengerService.getAllPassengers();
        PassengerDtoList passengerDtoList = new PassengerDtoList(passengerList);
        return ResponseEntity.ok(passengerDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDto> readById(@PathVariable Long id) {
        PassengerDto passengerDTO = passengerService.readById(id);
        if (passengerDTO != null) {
            return ResponseEntity.ok(passengerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/lastName")
    public ResponseEntity<List<PassengerDto>> readByLastName(@RequestParam("lastName") String lastName) {
        List<PassengerDto> passengers = passengerService.readByLastName(lastName);
        if (!passengers.isEmpty()) {
            return ResponseEntity.ok(passengers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerDto> update(
            @RequestBody PassengerDto dto,
            @PathVariable Long id
    ) {
        PassengerDto updatedPassengerDTO = passengerService.update(dto, id);
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
    @GetMapping("/available")
    public List<PassengerDto> getAvailablePassengers() {
        List<PassengerDto> passengerDTOs = passengerService.findAvailablePassenger();
        return passengerDTOs;
    }
    @PutMapping("/{id}/toggle-availability")
    public ResponseEntity<PassengerDto> toggleDriverAvailability(@PathVariable("id") Long id) {
        PassengerDto updatedPassengerDTO = passengerService.toggleAvailability(id);
        if (updatedPassengerDTO != null) {
            return ResponseEntity.ok(updatedPassengerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PreDestroy
    public void destroy() {
        producer.close();
    }
}