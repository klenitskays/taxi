package com.example.passenger.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Entity
@Table(name = "passenger")
@Data
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Contact info is required")
    @Size(max = 100, message = "Contact info cannot exceed 100 characters")
    @Column(name = "contact_info")
    private String contactInfo;

    @Min(value = -180, message = "Start latitude must be greater than or equal to -180")
    @Max(value = 180, message = "Start latitude must be less than or equal to 180")
    @Column(name = "start_latitude")
    private Double startLatitude;

    @Min(value = -180, message = "Start longitude must be greater than or equal to -180")
    @Max(value = 180, message = "Start longitude must be less than or equal to 180")
    @Column(name = "start_longitude")
    private Double startLongitude;

    @Min(value = -180, message = "Destination latitude must be greater than or equal to -180")
    @Max(value = 180, message = "Destination latitude must be less than or equal to 180")
    @Column(name = "destination_latitude")
    private Double destinationLatitude;

    @Min(value = -180, message = "Destination longitude must be greater than or equal to -180")
    @Max(value = 180, message = "Destination longitude must be less than or equal to 180")
    @Column(name = "destination_longitude")
    private Double destinationLongitude;

    @Min(value = 0, message = "Rating must be a non-negative value")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(name = "rating")
    private Integer rating;
}