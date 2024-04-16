package com.example.passenger;

import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
import com.example.passenger.service.PassengerService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class PassengerIntegrationTests {
    @MockBean
    private PassengerService passengerService;

    @Mock
    private KafkaProducer<String, String> kafkaProducer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void testCreatePassenger() {
        PassengerDto passengerDto = new PassengerDto("John", "Doe");
        PassengerDto createdPassengerDto = new PassengerDto(1, "John", "Doe");

        when(passengerService.create(any(PassengerDto.class))).thenReturn(createdPassengerDto);

        given()
                .contentType(ContentType.JSON)
                .body(passengerDto)
                .when()
                .post("/passenger")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", equalTo(createdPassengerDto.getId()))
                .body("firstName", equalTo(createdPassengerDto.getFirstName()))
                .body("lastName", equalTo(createdPassengerDto.getLastName()));

        verify(passengerService, times(1)).create(any(PassengerDto.class));
    }

    @Test
    public void testGetAllPassengers() {
        PassengerDto passengerDto = new PassengerDto(1, "John", "Doe");
        List<PassengerDto> passengerDtoList = Collections.singletonList(passengerDto);
        PassengerDtoList passengerDtoListWrapper = new PassengerDtoList(passengerDtoList);

        when(passengerService.getAllPassengers()).thenReturn(passengerDtoList);

        given()
                .when()
                .get("/passenger")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("passengers.size()", equalTo(1))
                .body("passengers[0].id", equalTo(passengerDto.getId()))
                .body("passengers[0].firstName", equalTo(passengerDto.getFirstName()))
                .body("passengers[0].lastName", equalTo(passengerDto.getLastName()));

        verify(passengerService, times(1)).getAllPassengers();
    }

    @Test
    public void testGetPassengerById() {
        PassengerDto passengerDto = new PassengerDto(1, "John", "Doe");

        when(passengerService.readById(anyLong())).thenReturn(passengerDto);

        given()
                .when()
                .get("/passenger/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(passengerDto.getId()))
                .body("firstName", equalTo(passengerDto.getFirstName()))
                .body("lastName", equalTo(passengerDto.getLastName()));

        verify(passengerService, times(1)).readById(anyLong());
    }

    @Test
    public void testGetPassengersByLastName() {
        PassengerDto passengerDTO = new PassengerDto(1, "John", "Doe");
        List<PassengerDto> passengerDTOList = Collections.singletonList(passengerDTO);

        when(passengerService.readByLastName(anyString())).thenReturn(passengerDTOList);

        given()
                .param("lastName", "Doe")
                .when()
                .get("/passenger/lastName")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1))
                .body("[0].id", equalTo(passengerDTO.getId()))
                .body("[0].firstName", equalTo(passengerDTO.getFirstName()))
                .body("[0].lastName", equalTo(passengerDTO.getLastName()));

        verify(passengerService, times(1)).readByLastName(anyString());
    }

    @Test
    public void testUpdatePassenger() {
        PassengerDto passengerDto = new PassengerDto(1, "John", "Doe");
        PassengerDto updatedPassengerDto = new PassengerDto(1, "Jane", "Smith");

        when(passengerService.update(any(PassengerDto.class), anyLong())).thenReturn(updatedPassengerDto);

        given()
                .contentType(ContentType.JSON)
                .body(passengerDto)
                .when()
                .put("/passenger/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(updatedPassengerDto.getId()))
                .body("firstName", equalTo(updatedPassengerDto.getFirstName()))
                .body("lastName", equalTo(updatedPassengerDto.getLastName()));

        verify(passengerService, times(1)).update(any(PassengerDto.class), anyLong());
    }
    @Test
    public void testDeletePassenger() {
        long passengerId = 1;

        doNothing().when(passengerService).delete(passengerId);

        given()
                .when()
                .delete("/passenger/{id}", passengerId)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        verify(passengerService, times(1)).delete(passengerId);
    }
}