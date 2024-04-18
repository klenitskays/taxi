package com.example.passenger;

import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    private ResponseEntity<PassengerDtoList> responseEntityList;
    private List<PassengerDto> allPassengers;
    private ResponseEntity<Void> responseEntityDelete;
    private ResponseEntity<List<PassengerDto>> responseEntityLastName;
    private List<PassengerDto> availablePassengers;
    private ResponseEntity<List<PassengerDto>> responseEntityAvailable;
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

    @Given("The request for getting all passengers")
    public void theRequestForGettingAllPassengers() {
        List<PassengerDto> passengers = new ArrayList<>();
        PassengerDto passenger1 = new PassengerDto();
        passenger1.setId(1);
        passenger1.setFirstName("John");
        passenger1.setLastName("Doe");
        passengers.add(passenger1);
        PassengerDto passenger2 = new PassengerDto();
        passenger2.setId(2);
        passenger2.setFirstName("Jane");
        passenger2.setLastName("Smith");
        passengers.add(passenger2);

        when(passengerService.getAllPassengers()).thenReturn(passengers);
    }

    @When("Get all passengers")
    public void getAllPassengers() {
        responseEntityList = passengerController.getAllPassengers();
        allPassengers = responseEntityList.getBody().getPassengers();
    }

    @Then("A response with all passengers")
    public void aResponseWithAllPassengers() {
        System.out.println("All passengers: " + allPassengers);
        Assertions.assertEquals(HttpStatus.OK, responseEntityList.getStatusCode());
        Assertions.assertNotNull(allPassengers);
        Assertions.assertEquals(2, allPassengers.size());

        PassengerDto passenger1 = allPassengers.get(0);
        Assertions.assertEquals(1, passenger1.getId());
        Assertions.assertEquals("John", passenger1.getFirstName());
        Assertions.assertEquals("Doe", passenger1.getLastName());

        PassengerDto passenger2 = allPassengers.get(1);
        Assertions.assertEquals(2, passenger2.getId());
        Assertions.assertEquals("Jane", passenger2.getFirstName());
        Assertions.assertEquals("Smith", passenger2.getLastName());
    }

    @Given("The request for deleting passenger by id")
    public void theRequestForDeletingPassengerById() {
        doNothing().when(passengerService).delete(any(Long.class));
    }

    @When("Delete passenger by id")
    public void deletePassengerById() {
        responseEntityDelete = passengerController.delete(1L);
    }

    @Then("A response with HTTP status 204 No Content")
    public void aResponseWithHttpStatusNoContent() {
        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntityDelete.getStatusCode());
    }
    @Given("The request for reading passengers by last name")
    public void theRequestForReadingPassengersByLastName() {
        String lastName = "Doe";
        List<PassengerDto> passengers = Arrays.asList(
                new PassengerDto(1, "John", lastName),
                new PassengerDto(2, "Jane", lastName)
        );
        when(passengerService.readByLastName(lastName)).thenReturn(passengers);
    }

    @When("Read passengers by last name")
    public void readPassengersByLastName() {
        String lastName = "Doe";
        responseEntityLastName = passengerController.readByLastName(lastName);
    }

    @Then("A response with a list of passengers with the last name")
    public void aResponseWithListOfPassengersWithTheLastName() {
        List<PassengerDto> passengers = responseEntityLastName.getBody();
        Assertions.assertEquals(HttpStatus.OK, responseEntityLastName.getStatusCode());
        Assertions.assertNotNull(passengers);
        Assertions.assertFalse(passengers.isEmpty());
        Assertions.assertTrue(passengers.stream().allMatch(p -> p.getLastName().equals("Doe")));
    }
}