package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.service.PassengerService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        // Arrange
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setFirstName("John");
        passengerDTO.setLastName("Doe");

        when(passengerService.create(any(PassengerDTO.class))).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.create(passengerDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    public void testGetAllPassengers() {
        // Arrange
        List<PassengerDTO> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDTO("John", "Doe"));
        passengerList.add(new PassengerDTO("Jane", "Smith"));
        Page<PassengerDTO> passengerPage = new PageImpl<>(passengerList);

        Pageable pageable = Pageable.unpaged();
        when(passengerService.getAllPassengers(eq(pageable))).thenReturn(passengerPage);

        // Act
        ResponseEntity<Page<PassengerDTO>> response = passengerController.getAllPassengers(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
    }

    @Test
    public void testReadById() {
        // Arrange
        Long passengerId = 1L;
        PassengerDTO passengerDTO = new PassengerDTO("John", "Doe");
        when(passengerService.readById(eq(passengerId))).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.readById(passengerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    public void testReadById_NotFound() {
        // Arrange
        Long passengerId = 1L;
        when(passengerService.readById(eq(passengerId))).thenReturn(null);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.readById(passengerId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testReadByLastName() {
        // Arrange
        String lastName = "Doe";
        List<PassengerDTO> passengers = new ArrayList<>();
        passengers.add(new PassengerDTO("John", "Doe"));
        when(passengerService.readByLastName(eq(lastName))).thenReturn(passengers);

        // Act
        ResponseEntity<List<PassengerDTO>> response = passengerController.readByLastName(lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getFirstName());
        assertEquals("Doe", response.getBody().get(0).getLastName());
    }

    @Test
    public void testReadByLastName_NotFound() {
        // Arrange
        String lastName = "Doe";
        when(passengerService.readByLastName(eq(lastName))).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<PassengerDTO>> response = passengerController.readByLastName(lastName);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdatePassenger() {
        // Arrange
        Long passengerId = 1L;
        PassengerDTO passengerDTO = new PassengerDTO("John", "Doe");
        when(passengerService.update(any(PassengerDTO.class), eq(passengerId))).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.update(passengerDTO, passengerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    @Test
    public void testDeletePassenger() {
        // Arrange
        Long passengerId = 1L;

        // Act
        ResponseEntity<Void> response = passengerController.delete(passengerId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        // Дополнительная проверка: убедитесь, что метод delete был вызван с правильным параметром
        verify(passengerService, times(1)).delete(eq(passengerId));
    }

    @Test
    public void testDeletePassenger_NotFound() {
        // Arrange
        Long passengerId = 1L;
        doThrow(new EntityNotFoundException()).when(passengerService).delete(eq(passengerId));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> passengerController.delete(passengerId));

        // Дополнительная проверка: убедитесь, что метод delete был вызван с правильным параметром
        verify(passengerService, times(1)).delete(eq(passengerId));
    }

    @Test
    public void testGetAvailablePassengers() {
        // Arrange
        List<PassengerDTO> passengerList = new ArrayList<>();
        passengerList.add(new PassengerDTO("John", "Doe"));
        passengerList.add(new PassengerDTO("Jane", "Smith"));
        when(passengerService.findAvailablePassenger()).thenReturn(passengerList);

        // Act
        List<PassengerDTO> response = passengerController.getAvailablePassengers();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(passengerList, response);
    }

    @Test
    public void testTogglePassengerAvailability() {
        // Arrange
        Long passengerId = 1L;
        PassengerDTO passengerDTO = new PassengerDTO("John", "Doe");
        when(passengerService.toggleAvailability(eq(passengerId))).thenReturn(passengerDTO);

        // Act
        ResponseEntity<PassengerDTO> response = passengerController.toggleDriverAvailability(passengerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }
}
