package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
import com.example.passenger.service.PassengerService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PassengerJUnitTests {
    private PassengerController passengerController;

    @Mock
    private PassengerService passengerService;

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    private static final String TOPIC_NAME = "test_topic";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        passengerController = new PassengerController(passengerService, kafkaProducer, TOPIC_NAME);
    }

    @Test
    public void testCreatePassenger() {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");

        when(passengerService.create(any(PassengerDto.class))).thenReturn(passengerDto);

        ResponseEntity<PassengerDto> response = passengerController.create(passengerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    public void testGetAllPassengers() {
        List<PassengerDto> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDto("John", "Doe"));
        passengerList.add(new PassengerDto("Jane", "Smith"));
        PassengerDtoList passengerDtoList = new PassengerDtoList(passengerList);

        when(passengerService.getAllPassengers()).thenReturn(passengerList);

        ResponseEntity<PassengerDtoList> response = passengerController.getAllPassengers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getPassengers().size());
    }

    @Test
    public void testReadById() {
        Long passengerId = 1L;
        PassengerDto passengerDTO = new PassengerDto("John", "Doe");
        when(passengerService.readById(eq(passengerId))).thenReturn(passengerDTO);

        ResponseEntity<PassengerDto> response = passengerController.readById(passengerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    public void testReadById_NotFound() {
        Long passengerId = 1L;
        when(passengerService.readById(eq(passengerId))).thenReturn(null);

        ResponseEntity<PassengerDto> response = passengerController.readById(passengerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testReadByLastName() {
        String lastName = "Doe";
        List<PassengerDto> passengers = new ArrayList<>();
        passengers.add(new PassengerDto("John", "Doe"));
        when(passengerService.readByLastName(eq(lastName))).thenReturn(passengers);

        ResponseEntity<List<PassengerDto>> response = passengerController.readByLastName(lastName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getFirstName());
        assertEquals("Doe", response.getBody().get(0).getLastName());
    }

    @Test
    public void testReadByLastName_NotFound() {
        String lastName = "Doe";
        when(passengerService.readByLastName(eq(lastName))).thenReturn(new ArrayList<>());

        ResponseEntity<List<PassengerDto>> response = passengerController.readByLastName(lastName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdatePassenger() {
        Long passengerId = 1L;
        PassengerDto passengerDto = new PassengerDto("John", "Doe");
        when(passengerService.update(any(PassengerDto.class), eq(passengerId))).thenReturn(passengerDto);

        ResponseEntity<PassengerDto> response = passengerController.update(passengerDto, passengerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    public void testDeletePassenger() {
        Long passengerId = 1L;

        ResponseEntity<Void> response = passengerController.delete(passengerId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(passengerService, times(1)).delete(eq(passengerId));
    }

    @Test
    public void testDeletePassenger_NotFound() {
        Long passengerId = 1L;
        doThrow(new EntityNotFoundException()).when(passengerService).delete(eq(passengerId));

        assertThrows(EntityNotFoundException.class, () -> passengerController.delete(passengerId));

        verify(passengerService, times(1)).delete(eq(passengerId));
    }

    @Test
    public void testGetAvailablePassengers() {
        List<PassengerDto> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDto("John", "Doe"));
        passengerList.add(new PassengerDto("Jane", "Smith"));
        when(passengerService.findAvailablePassenger()).thenReturn(passengerList);

        List<PassengerDto> response = passengerController.getAvailablePassengers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(passengerList, response);
    }

    @Test
    public void testTogglePassengerAvailability() {
        Long passengerId = 1L;
        PassengerDto passengerDTO = new PassengerDto("John", "Doe");
        when(passengerService.toggleAvailability(eq(passengerId))).thenReturn(passengerDTO);

        ResponseEntity<PassengerDto> response = passengerController.toggleDriverAvailability(passengerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }
}
