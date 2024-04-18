Feature: Passenger Service

  Scenario: Saving new passenger with valid request
    Given The request for saving new passenger
    When Save a new passenger
    Then A response with id of created passenger

  Scenario: Reading passenger by ID
    Given The request for reading passenger by id
    When Read passenger by id
    Then A response with the passenger details

  Scenario: Updating passenger by id
    Given The request for updating passenger by id
    When Update passenger by id
    Then A response with the updated passenger details

  Scenario: Get all passengers
    Given The request for getting all passengers
    When Get all passengers
    Then A response with all passengers

  Scenario: Deleting a passenger by ID
    Given The request for deleting passenger by id
    When Delete passenger by id
    Then A response with HTTP status 204 No Content

  Scenario: Reading passengers by last name
    Given The request for reading passengers by last name
    When Read passengers by last name
    Then A response with a list of passengers with the last name
