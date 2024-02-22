package com.example.driver.mapper;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface DriverMapper {
    DriverMapper INSTANCE = Mappers.getMapper(DriverMapper.class);

    @Mapping(target = "id", ignore = true)
    DriverDTO toDriverDTO(Driver driver);

    @Mapping(target = "id", ignore = true)
    Driver toDriver(DriverDTO dto);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    void updateDriverFromDTO(DriverDTO dto, @MappingTarget Driver driver);
}