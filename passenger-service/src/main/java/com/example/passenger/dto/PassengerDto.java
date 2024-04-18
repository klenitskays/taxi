package com.example.passenger.dto;

import lombok.Data;

@Data
public class PassengerDto {
    private int id;
    private String firstName;
    private String lastName;
    private String contactInfo;
    private double startLatitude;
    private double startLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private boolean available;
    private int rating;
    public PassengerDto() {
    }

    public PassengerDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public PassengerDto(long id, String firstName, String lastName) {
        this.id = (int) id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}