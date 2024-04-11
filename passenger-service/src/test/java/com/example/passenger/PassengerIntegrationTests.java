package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.service.PassengerService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PassengerIntegrationTests {
    private MockMvc mockMvc;

    @Autowired
    private PassengerController passengerController;

    @MockBean
    private PassengerService passengerService;

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCreatePassenger() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO("John", "Doe");
        PassengerDTO createdPassengerDTO = new PassengerDTO(1, "John", "Doe");

        doReturn(createdPassengerDTO).when(passengerService).create(any(PassengerDTO.class));

        mockMvc.perform(post("/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdPassengerDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(createdPassengerDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(createdPassengerDTO.getLastName()));
    }

    @Test
    public void testGetAllPassengers() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO(1, "John", "Doe");
        List<PassengerDTO> passengerDTOList = Collections.singletonList(passengerDTO);

        when(passengerService.getAllPassengers())
                .thenReturn(passengerDTOList);

        mockMvc.perform(get("/passenger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(passengerDTO.getId()))
                .andExpect(jsonPath("$[0].firstName").value(passengerDTO.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(passengerDTO.getLastName()));
    }

    @Test
    public void testGetPassengerById() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO(1, "John", "Doe");

        when(passengerService.readById(anyLong()))
                .thenReturn(passengerDTO);

        mockMvc.perform(get("/passenger/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(passengerDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(passengerDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(passengerDTO.getLastName()));
    }

    @Test
    public void testGetPassengersByLastName() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO(1, "John", "Doe");
        List<PassengerDTO> passengerDTOList = Collections.singletonList(passengerDTO);

        when(passengerService.readByLastName(anyString()))
                .thenReturn(passengerDTOList);

        mockMvc.perform(get("/passenger/lastName")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(passengerDTO.getId()))
                .andExpect(jsonPath("$[0].firstName").value(passengerDTO.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(passengerDTO.getLastName()));
    }

    @Test
    public void testUpdatePassenger() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO(1, "John", "Doe");
        PassengerDTO updatedPassengerDTO = new PassengerDTO(1, "Jane", "Smith");

        when(passengerService.update(any(PassengerDTO.class), anyLong()))
                .thenReturn(updatedPassengerDTO);

        mockMvc.perform(put("/passenger/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Jane\",\"lastName\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedPassengerDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(updatedPassengerDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updatedPassengerDTO.getLastName()));
    }

    @Test
    public void testDeletePassenger() throws Exception {
        doNothing().when(passengerService).delete(anyLong());

        mockMvc.perform(delete("/passenger/{id}", 1))
                .andExpect(status().isNoContent());
    }
}