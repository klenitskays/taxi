package com.example.driver.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "driver")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "is_available")
    private boolean isAvailable;

    // Конструкторы, геттеры, сеттеры и другие методы

    //...
}