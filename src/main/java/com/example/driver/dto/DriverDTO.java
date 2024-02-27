package com.example.driver.dto;

import com.example.driver.entity.Driver;
import lombok.Data;

@Data
public class DriverDTO extends Driver {
    private int id;
    private String firstName;
    private String lastName;
    private String contactInfo;
    private double latitude;
    private double longitude;
    private int rating;
    private boolean available;

}
