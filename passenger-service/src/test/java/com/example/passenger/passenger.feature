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
