package com.example.driver.dto;

import java.util.List;

public class DriverListDto {
    private List<DriverDto> drivers;

    public DriverListDto(List<DriverDto> drivers) {
        this.drivers = drivers;
    }

    public List<DriverDto> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverDto> drivers) {
        this.drivers = drivers;
    }
}
