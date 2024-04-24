package com.example.passenger.dto;

import java.util.List;
import java.util.Objects;

public class PassengerDtoList {
    private List<PassengerDto> passengers;

    public PassengerDtoList(List<PassengerDto> passengers) {
        this.passengers = passengers;
    }

    public List<PassengerDto> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDto> passengers) {
        this.passengers = passengers;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerDtoList that = (PassengerDtoList) o;
        return Objects.equals(passengers, that.passengers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengers);
    }
}