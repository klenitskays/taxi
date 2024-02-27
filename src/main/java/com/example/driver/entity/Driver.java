package com.example.driver.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "driver")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "rating")
    private Integer rating;

}