package com.example.driver.dto;

import lombok.Data;

@Data
public class DriverDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String contactInfo;
    private double latitude;
    private double longitude;
    private int rating;
    private boolean available;

}
