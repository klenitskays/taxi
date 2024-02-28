package com.example.passenger.dto;

import lombok.Data;

@Data
public class PassengerDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String contactInfo;
    private double startLatitude;
    private double startLongitude;
    private double destinationLatitude;
    private double destinationLongitude;
    private int rating;
}
