package com.example.rideservice.dto;

import com.example.rideservice.status.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RideDTO {
    private Integer id;
    private Integer passengerId;
    private Integer driverId;
    private Double startLatitude;
    private Double startLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private RideStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
