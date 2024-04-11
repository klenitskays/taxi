package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.kafka.KafkaConfig;
import com.example.passenger.service.PassengerService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = KafkaConfig.class)
public class PassengerMockitoSpyTests {

    @Mock
    private PassengerService passengerService;
    private KafkaProducer<String, String> producer;

    private PassengerController passengerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        String topicName = "test-topic";

        // Create a mock KafkaProducer
        producer = mock(KafkaProducer.class);

        passengerController = new PassengerController(passengerService, producer, topicName);
    }

    @Test
    @DisplayName("Test create method")
    void testCreate() throws Exception {
        // Arrange
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setFirstName("John");
        passengerDTO.setLastName("Doe");

        PassengerDTO createdPassengerDTO = new PassengerDTO();
        createdPassengerDTO.setFirstName("John");
        createdPassengerDTO.setLastName("Doe");

        when(passengerService.create(passengerDTO)).thenReturn(createdPassengerDTO);

        // Create Kafka producer configurations
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Create a real instance of KafkaProducer
        KafkaProducer<String, String> realProducer = new KafkaProducer<>(properties);

        // Spy the real instance of KafkaProducer
        KafkaProducer<String, String> spiedProducer = spy(realProducer);

        // Use reflection to set the spiedProducer in passengerController
        Field producerField = PassengerController.class.getDeclaredField("producer");
        producerField.setAccessible(true);
        producerField.set(passengerController, spiedProducer);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.create(passengerDTO);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdPassengerDTO);

        // Verify the interaction with KafkaProducer
        verify(spiedProducer).send(any(), any());
    }

    @Test
    @DisplayName("Test getAllPassengers method")
    void testGetAllPassengers() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        List<PassengerDTO> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDTO("John", "Doe"));
        passengerList.add(new PassengerDTO("Jane", "Smith"));

        when(passengerService.getAllPassengers()).thenReturn(passengerList);

        // Act
        ResponseEntity<List<PassengerDTO>> response = passengerController.getAllPassengers();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerList);
    }

    @Test
    @DisplayName("Test readById method with existing passenger")
    void testReadByIdExistingPassenger() {
        // Arrange
        Long passengerId = 1L;
        PassengerDTO passengerDTO = new PassengerDTO();
        when(passengerService.readById(passengerId)).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.readById(passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerDTO);
    }

    @Test
    @DisplayName("Test readById method with non-existing passenger")
    void testReadByIdNonExistingPassenger() {
        // Arrange
        Long passengerId = 1L;
        when(passengerService.readById(passengerId)).thenReturn(null);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.readById(passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test readByLastName method with existing passengers")
    void testReadByLastNameExistingPassengers() {
        // Arrange
        String lastName = "Doe";
        List<PassengerDTO> passengers = Arrays.asList(new PassengerDTO(), new PassengerDTO());
        when(passengerService.readByLastName(lastName)).thenReturn(passengers);

        // Act
        ResponseEntity<List<PassengerDTO>> response = passengerController.readByLastName(lastName);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengers);
    }

    @Test
    @DisplayName("Test readByLastName method with non-existing passengers")
    void testReadByLastNameNonExistingPassengers() {
        // Arrange
        String lastName = "Doe";
        when(passengerService.readByLastName(lastName)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<PassengerDTO>> response = passengerController.readByLastName(lastName);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test update method with existing passenger")
    void testUpdateExistingPassenger() {
        // Arrange
        Long passengerId= 1L;
        PassengerDTO passengerDTO = new PassengerDTO();

        when(passengerService.update(passengerDTO, passengerId)).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.update(passengerDTO, passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerDTO);
    }

    @Test
    @DisplayName("Test update method with non-existing passenger")
    void testUpdateNonExistingPassenger() {
        // Arrange
        Long passengerId = 1L;
        PassengerDTO passengerDTO = new PassengerDTO();

        when(passengerService.update(passengerDTO, passengerId)).thenReturn(null);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.update(passengerDTO, passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test delete method")
    void testDelete() {
        // Arrange
        Long passengerId = 1L;

        // Act
        ResponseEntity<Void> response = passengerController.delete(passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        // Verify that the delete method was called
        verify(passengerService).delete(passengerId);
    }

    @Test
    @DisplayName("Test getAvailablePassengers method")
    void testGetAvailablePassengers() {
        // Arrange
        List<PassengerDTO> availablePassengers = Arrays.asList(new PassengerDTO(), new PassengerDTO());
        when(passengerService.findAvailablePassenger()).thenReturn(availablePassengers);

        // Act
        List<PassengerDTO> response = passengerController.getAvailablePassengers();

        // Assert
        assertThat(response).isEqualTo(availablePassengers);
    }

    @Test
    @DisplayName("Test toggleDriverAvailability method with existing passenger")
    void testToggleDriverAvailabilityExistingPassenger() {
        // Arrange
        Long passengerId = 1L;
        PassengerDTO passengerDTO = new PassengerDTO();

        when(passengerService.toggleAvailability(passengerId)).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.toggleDriverAvailability(passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerDTO);
    }

    @Test
    @DisplayName("Test toggleDriverAvailability method with non-existing passenger")
    void testToggleDriverAvailabilityNonExistingPassenger() {
        // Arrange
        Long passengerId = 1L;

        when(passengerService.toggleAvailability(passengerId)).thenReturn(null);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.toggleDriverAvailability(passengerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test destroy method")
    void testDestroy() {
        // Act
        passengerController.destroy();

        // Verify that the producer was closed
        verify(producer).close();
    }
}
