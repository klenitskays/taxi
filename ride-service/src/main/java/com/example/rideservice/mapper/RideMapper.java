package com.example.rideservice.mapper;

import com.example.rideservice.dto.RideDto;
import com.example.rideservice.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RideMapper {

    RideDto toRideDto(Ride ride);

    List<RideDto> toRideDto(List<Ride> rides);

    Ride toRide(RideDto dto);

    @Mapping(target = "startLatitude", source = "startLongitude")
    @Mapping(target = "destinationLatitude", source = "destinationLongitude")
    void updateRideFromDto(RideDto dto, @MappingTarget Ride ride);
}