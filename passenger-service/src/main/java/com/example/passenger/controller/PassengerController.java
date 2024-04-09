package com.example.passenger.controller;

import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.service.PassengerService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;
    private final KafkaProducer<String, String> producer;
    private final String topicName;
    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        this.producer = new KafkaProducer<>(props);
        this.topicName = "my-topic";
    }
    @PostMapping
    public ResponseEntity<PassengerDTO> create(@RequestBody PassengerDTO dto) {
        PassengerDTO createdPassengerDTO = passengerService.create(dto);

        String message = "Новый пассажир создан: " + createdPassengerDTO.getFirstName() + " " + createdPassengerDTO.getLastName();
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                // Обработка ошибки отправки сообщения
                exception.printStackTrace();
            } else {
                // Успешная отправка сообщения
                System.out.println("Сообщение успешно отправлено в Kafka. Topic: " + metadata.topic() +
                        ", Partition: " + metadata.partition() + ", Offset: " + metadata.offset());
            }
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPassengerDTO);
    }

    @GetMapping
    public List<PassengerDTO> getAllPassengers(@RequestParam int pageNumber, @RequestParam int pageSize) {
        Page<PassengerDTO> passengerPage = passengerService.getAllPassengers(PageRequest.of(pageNumber,pageSize));
        return passengerPage.getContent();
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
    @GetMapping("/available")
    public List<PassengerDTO> getAvailablePassengers() {
        List<PassengerDTO> passengerDTOs = passengerService.findAvailablePassenger();
        return passengerDTOs;
    }
    @PutMapping("/{id}/toggle-availability")
    public ResponseEntity<PassengerDTO> toggleDriverAvailability(@PathVariable("id") Long id) {
        PassengerDTO updatedPassengerDTO = passengerService.toggleAvailability(id);
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