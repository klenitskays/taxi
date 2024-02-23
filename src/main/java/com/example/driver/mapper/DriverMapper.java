package com.example.driver.mapper;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface DriverMapper {

    DriverDTO toDriverDTO(Driver driver);

    Driver toDriver(DriverDTO dto);


    void updateDriverFromDTO(DriverDTO dto, @MappingTarget Driver driver);
}