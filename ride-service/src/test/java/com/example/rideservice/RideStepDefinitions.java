package com.example.rideservice;

import com.example.rideservice.controller.RideController;
import com.example.rideservice.dto.RideDto;
import com.example.rideservice.service.RideService;
import com.example.rideservice.status.RideStatus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class RideStepDefinitions {

    @Mock
    private RideService rideService;

    @InjectMocks
    private RideController rideController;

    private ResponseEntity<RideDto> responseEntity;
    private RideDto rideDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public RideStepDefinitions() {
        MockitoAnnotations.openMocks(this);
        rideController = new RideController(rideService);
    }

    @Given("The request for creating a new ride")
    public void theRequestForCreatingNewRide() {
        RideDto rideDto = new RideDto();
        rideDto.setId(1);
        rideDto.setPassengerId(1);
        rideDto.setDriverId(1);
        rideDto.setStartLatitude(0.0);
        rideDto.setStartLongitude(0.0);
        rideDto.setDestinationLatitude(1.0);
        rideDto.setDestinationLongitude(1.0);
        rideDto.setStatus(RideStatus.CREATED);
        rideDto.setStartTime(LocalDateTime.now());

        when(rideService.createRide(any(RideDto.class))).thenReturn(rideDto);
    }

    @When("Create a new ride")
    public void createANewRide() {
        RideDto requestDto = new RideDto();
        responseEntity = rideController.createRide(requestDto);
    }

    @Then("A response with id of created ride")
    public void aResponseWithIdOfCreatedRide() {
        RideDto createdRideDto = responseEntity.getBody();
        assertNotNull(createdRideDto);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertTrue(createdRideDto.getId() > 0);
    }

    @Then("A response with HTTP status 404 Not Found")
    public void aResponseWithHttpStatusNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Given("The request for retrieving a ride by ID")
    public void theRequestForRetrievingARideByID() {
        rideDto = createSampleRideDto(1);
        when(rideService.getRideById(1)).thenReturn(rideDto);
    }

    @When("Get a ride by ID")
    public void getARideByID() {
        responseEntity = rideController.getRideById(1);
    }

    @Then("A response with the ride details")
    public void aResponseWithTheRideDetails() {
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getId());
    }

    @Then("The ride exists")
    public void theRideExists() {
        assertNotNull(responseEntity.getBody());
    }

    @Given("The request for retrieving a non-existent ride by ID")
    public void theRequestForRetrievingANonExistentRideByID() {
        when(rideService.getRideById(999)).thenReturn(null);
    }

    private RideDto createSampleRideDto(int id) {
        RideDto rideDto = new RideDto();
        rideDto.setId(id);
        rideDto.setPassengerId(1);
        rideDto.setDriverId(1);
        rideDto.setStartLatitude(0.0);
        rideDto.setStartLongitude(0.0);
        rideDto.setDestinationLatitude(1.0);
        rideDto.setDestinationLongitude(1.0);
        rideDto.setStatus(RideStatus.CREATED);
        rideDto.setStartTime(LocalDateTime.now());
        rideDto.setPrice(990);
        return rideDto;
    }
    @Given("The request for completing a ride")
    public void theRequestForCompletingARide() {
        RideDto rideDto = createSampleRideDto(1);
        when(rideService.completeRide(any(Integer.class))).thenReturn(rideDto);
    }

    @When("Complete the ride")
    public void completeTheRide() {
        responseEntity = rideController.completeRide(1);
    }

    @Then("A response with the completed ride details")
    public void aResponseWithTheCompletedRideDetails() {
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getId());
    }

}