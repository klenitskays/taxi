package com.example.driver.mapper;

import com.example.driver.dto.DriverDto;
import com.example.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverDto toDriverDto(Driver driver);

    List<DriverDto> toDriverDto(List<Driver> drivers);

    Driver toDriver(DriverDto dto);
    @Mapping(target = "firstName", source = "driverName")
    default void updateDriverFromDto(DriverDto dto,@MappingTarget Driver driver) {
        driver.setFirstName(dto.getFirstName());
        driver.setLastName(dto.getLastName());
        driver.setContactInfo(dto.getContactInfo());
        driver.setLatitude(dto.getLatitude());
        driver.setLongitude(dto.getLongitude());
        driver.setAvailable(dto.isAvailable());
        driver.setRating(dto.getRating());
    }
}