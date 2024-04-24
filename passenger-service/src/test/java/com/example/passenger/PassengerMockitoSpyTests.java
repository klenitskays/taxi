package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
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

        producer = mock(KafkaProducer.class);

        passengerController = new PassengerController(passengerService, producer, topicName);
    }

    @Test
    @DisplayName("Test create method")
    void testCreate() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");

        PassengerDto createdPassengerDto = new PassengerDto();
        createdPassengerDto.setFirstName("John");
        createdPassengerDto.setLastName("Doe");

        when(passengerService.create(passengerDto)).thenReturn(createdPassengerDto);

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> realProducer = new KafkaProducer<>(properties);

        KafkaProducer<String, String> spiedProducer = spy(realProducer);

        Field producerField = PassengerController.class.getDeclaredField("producer");
        producerField.setAccessible(true);
        producerField.set(passengerController, spiedProducer);

        ResponseEntity<PassengerDto> response = passengerController.create(passengerDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdPassengerDto);

        verify(spiedProducer).send(any(), any());
    }

    @Test
    @DisplayName("Test getAllPassengers method")
    void testGetAllPassengers() {
        Pageable pageable = mock(Pageable.class);
        List<PassengerDto> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDto("John", "Doe"));
        passengerList.add(new PassengerDto("Jane", "Smith"));
        PassengerDtoList passengerDtoList = new PassengerDtoList(passengerList);

        when(passengerService.getAllPassengers()).thenReturn(passengerList);

        ResponseEntity<PassengerDtoList> response = passengerController.getAllPassengers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPassengers()).isEqualTo(passengerList);
    }


    @Test
    @DisplayName("Test readById method with existing passenger")
    void testReadByIdExistingPassenger() {
        Long passengerId = 1L;
        PassengerDto passengerDto = new PassengerDto();
        when(passengerService.readById(passengerId)).thenReturn(passengerDto);

        ResponseEntity<PassengerDto> response = passengerController.readById(passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerDto);
    }

    @Test
    @DisplayName("Test readById method with non-existing passenger")
    void testReadByIdNonExistingPassenger() {
        Long passengerId = 1L;
        when(passengerService.readById(passengerId)).thenReturn(null);

        ResponseEntity<PassengerDto> response = passengerController.readById(passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test readByLastName method with existing passengers")
    void testReadByLastNameExistingPassengers() {
        String lastName = "Doe";
        List<PassengerDto> passengers = Arrays.asList(new PassengerDto(), new PassengerDto());
        when(passengerService.readByLastName(lastName)).thenReturn(passengers);

        ResponseEntity<List<PassengerDto>> response = passengerController.readByLastName(lastName);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengers);
    }

    @Test
    @DisplayName("Test readByLastName method with non-existing passengers")
    void testReadByLastNameNonExistingPassengers() {
        String lastName = "Doe";
        when(passengerService.readByLastName(lastName)).thenReturn(Collections.emptyList());

        ResponseEntity<List<PassengerDto>> response = passengerController.readByLastName(lastName);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test update method with existing passenger")
    void testUpdateExistingPassenger() {
        Long passengerId= 1L;
        PassengerDto passengerDto = new PassengerDto();

        when(passengerService.update(passengerDto, passengerId)).thenReturn(passengerDto);

        ResponseEntity<PassengerDto> response = passengerController.update(passengerDto, passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerDto);
    }

    @Test
    @DisplayName("Test update method with non-existing passenger")
    void testUpdateNonExistingPassenger() {
        Long passengerId = 1L;
        PassengerDto passengerDto = new PassengerDto();

        when(passengerService.update(passengerDto, passengerId)).thenReturn(null);

        ResponseEntity<PassengerDto> response = passengerController.update(passengerDto, passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test delete method")
    void testDelete() {
        Long passengerId = 1L;

        ResponseEntity<Void> response = passengerController.delete(passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        verify(passengerService).delete(passengerId);
    }

    @Test
    @DisplayName("Test getAvailablePassengers method")
    void testGetAvailablePassengers() {
        List<PassengerDto> availablePassengers = Arrays.asList(new PassengerDto(), new PassengerDto());
        when(passengerService.findAvailablePassenger()).thenReturn(availablePassengers);

        List<PassengerDto> response = passengerController.getAvailablePassengers();

        assertThat(response).isEqualTo(availablePassengers);
    }

    @Test
    @DisplayName("Test toggleDriverAvailability method with existing passenger")
    void testToggleDriverAvailabilityExistingPassenger() {
        Long passengerId = 1L;
        PassengerDto passengerDto = new PassengerDto();

        when(passengerService.toggleAvailability(passengerId)).thenReturn(passengerDto);

        ResponseEntity<PassengerDto> response = passengerController.toggleDriverAvailability(passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(passengerDto);
    }

    @Test
    @DisplayName("Test toggleDriverAvailability method with non-existing passenger")
    void testToggleDriverAvailabilityNonExistingPassenger() {
        Long passengerId = 1L;

        when(passengerService.toggleAvailability(passengerId)).thenReturn(null);

        ResponseEntity<PassengerDto> response = passengerController.toggleDriverAvailability(passengerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Test destroy method")
    void testDestroy() {
        passengerController.destroy();

        verify(producer).close();
    }
}
