package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDto;
import com.example.passenger.service.PassengerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PassengerStepDefinitions {

    @Mock
    private PassengerService passengerService;

    @Mock
    private KafkaProducer<String, String> producer;

    private String topicName = "my-topic";

    @InjectMocks
    private PassengerController passengerController;

    private ResponseEntity<PassengerDto> responseEntity;
    private PassengerDto createdPassengerDto;
    private PassengerDto updatedPassengerDto;


    public PassengerStepDefinitions() {
        MockitoAnnotations.openMocks(this);
        passengerController = new PassengerController(passengerService, producer, topicName);
    }

    @Given("The request for saving new passenger")
    public void theRequestForSavingNewPassenger() {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setId(1);
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");
        when(passengerService.create(any(PassengerDto.class))).thenReturn(passengerDto);
    }

    @When("Save a new passenger")
    public void saveANewPassenger() {
        PassengerDto requestDto = new PassengerDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        responseEntity = passengerController.create(requestDto);
        createdPassengerDto = responseEntity.getBody();
    }

    @Then("A response with id of created passenger")
    public void aResponseWithIdOfCreatedPassenger() {
        System.out.println("Created passenger: " + createdPassengerDto);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertNotNull(createdPassengerDto);
        Assertions.assertEquals("John", createdPassengerDto.getFirstName());
        Assertions.assertEquals("Doe", createdPassengerDto.getLastName());
        Assertions.assertTrue(createdPassengerDto.getId() > 0);
    }

    @Given("The request for reading passenger by id")
    public void theRequestForReadingPassengerById() {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setId(1);
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");
        when(passengerService.readById(1L)).thenReturn(passengerDto);
    }

    @When("Read passenger by id")
    public void readPassengerById() {
        responseEntity = passengerController.readById(1L);
        createdPassengerDto = responseEntity.getBody();
    }

    @Then("A response with the passenger details")
    public void aResponseWithThePassengerDetails() {
        System.out.println("Read passenger: " + createdPassengerDto);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(createdPassengerDto);
        Assertions.assertEquals("John", createdPassengerDto.getFirstName());
        Assertions.assertEquals("Doe", createdPassengerDto.getLastName());
        Assertions.assertEquals(1, createdPassengerDto.getId());
    }

    @Given("The request for updating passenger by id")
    public void theRequestForUpdatingPassengerById() {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setId(1);
        passengerDto.setFirstName("John");
        passengerDto.setLastName("Doe");
        when(passengerService.update(any(PassengerDto.class), any(Long.class))).thenReturn(passengerDto);
    }

    @When("Update passenger by id")
    public void updatePassengerById() {
        PassengerDto requestDto = new PassengerDto();
        requestDto.setId(1);
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        responseEntity = passengerController.update(requestDto, 1L);
        updatedPassengerDto = responseEntity.getBody();
    }

    @Then("A response with the updated passenger details")
    public void aResponseWithTheUpdatedPassengerDetails() {
        System.out.println("Updated passenger: " + updatedPassengerDto);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(updatedPassengerDto);
        Assertions.assertEquals("John", updatedPassengerDto.getFirstName());
        Assertions.assertEquals("Doe", updatedPassengerDto.getLastName());
        Assertions.assertEquals(1, updatedPassengerDto.getId());
    }

}