package com.example.driver.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.*;

@Entity
@Table(name = "driver")
@Data
public class Driver {

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

    @NotNull(message = "Latitude is required")
    @Min(value = -180, message = "Latitude must be between -90 and 90")
    @Max(value = 180, message = "Latitude must be between -90 and 90")
    @Column(name = "latitude")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    @Column(name = "longitude")
    private Double longitude;

    @NotNull(message = "Availability status is required")
    @Column(name = "available")
    private Boolean available;

    @Min(value = 0, message = "Rating must be a non-negative value")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(name = "rating")
    private Integer rating;
}