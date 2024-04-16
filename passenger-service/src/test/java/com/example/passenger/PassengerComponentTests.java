package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
import com.example.passenger.service.PassengerService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PassengerComponentTests {

    private MockMvc mockMvc;

    @Mock
    private PassengerService passengerService;

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    @Value("${spring.kafka.template.default-topic}")
    private String topicName;

    private PassengerController passengerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passengerController = new PassengerController(passengerService, kafkaProducer, topicName);
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();
    }

    @Test
    void createPassenger_shouldReturnCreatedPassenger() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");

        when(passengerService.create(any(PassengerDto.class))).thenReturn(passengerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"));
        System.out.println("Значение topicName: " + topicName);
    }
    @Test
    void getAllPassengers_shouldReturnListOfPassengers() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");
        List<PassengerDto> passengerList = Collections.singletonList(passengerDto);
        PassengerDtoList passengerDtoList = new PassengerDtoList(passengerList);

        when(passengerService.getAllPassengers()).thenReturn(passengerList);

        mockMvc.perform(get("/passenger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passengers[0].firstName").value("John"))
                .andExpect(jsonPath("$.passengers[0].lastName").value("Doe"));
    }

    @Test
    void readPassengerById_existingPassengerId_shouldReturnPassenger() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");

        when(passengerService.readById(anyLong())).thenReturn(passengerDto);

        mockMvc.perform(get("/passenger/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void readPassengerById_nonExistingPassengerId_shouldReturnNotFound() throws Exception {
        when(passengerService.readById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/passenger/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void readPassengersByLastName_existingLastName_shouldReturnListOfPassengers() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");
        List<PassengerDto> passengerList = Collections.singletonList(passengerDto);

        when(passengerService.readByLastName(anyString())).thenReturn(passengerList);

        mockMvc.perform(get("/passenger/lastName?lastName=Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void readPassengersByLastName_nonExistingLastName_shouldReturnNotFound() throws Exception {
        when(passengerService.readByLastName(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/passenger/lastName?lastName=Doe"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePassenger_existingPassengerId_shouldReturnUpdatedPassenger() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");

        when(passengerService.update(any(PassengerDto.class), anyLong())).thenReturn(passengerDto);

        mockMvc.perform(put("/passenger/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void updatePassenger_nonExistingPassengerId_shouldReturnNotFound() throws Exception {
        when(passengerService.update(any(PassengerDto.class), anyLong())).thenReturn(null);

        mockMvc.perform(put("/passenger/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getAvailablePassengers_shouldReturnListOfPassengers() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");
        List<PassengerDto> passengerList = Collections.singletonList(passengerDto);

        when(passengerService.findAvailablePassenger()).thenReturn(passengerList);

        mockMvc.perform(get("/passenger/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void toggleDriverAvailability_existingPassengerId_shouldReturnUpdatedPassenger() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");

        when(passengerService.toggleAvailability(anyLong())).thenReturn(passengerDto);

        mockMvc.perform(put("/passenger/{id}/toggle-availability", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void toggleDriverAvailability_nonExistingPassengerId_shouldReturnNotFound() throws Exception {
        when(passengerService.toggleAvailability(anyLong())).thenReturn(null);

        mockMvc.perform(put("/passenger/{id}/toggle-availability", 1L))
                .andExpect(status().isNotFound());
    }
    @Test
    void deletePassenger_existingPassengerId_shouldReturnNoContent() throws Exception {
        doNothing().when(passengerService).delete(anyLong());

        mockMvc.perform(delete("/passenger/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePassenger_nonExistingPassengerId_shouldReturnNotFound() throws Exception {
        doThrow(new DataIntegrityViolationException("Passenger not found")).when(passengerService).delete(anyLong());

        mockMvc.perform(delete("/passenger/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}