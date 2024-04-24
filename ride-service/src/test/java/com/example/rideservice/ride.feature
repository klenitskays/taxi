Feature: Ride Service

  Scenario: Creating a new ride with available passenger
    Given The request for creating a new ride
    When Create a new ride
    Then A response with id of created ride

  Scenario: Retrieving a ride by ID
    Given The request for retrieving a ride by ID
    When Get a ride by ID
    Then A response with the ride details
    And The ride exists

  Scenario: Retrieving a non-existent ride by ID
    Given The request for retrieving a non-existent ride by ID
    When Get a ride by ID
    Then A response with HTTP status 404 Not Found

  Scenario: Completing a ride
    Given The request for completing a ride
    When Complete the ride
    Then A response with the completed ride details
