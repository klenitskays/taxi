package com.example.rideservice.mapper;

import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RideMapper {

    RideDTO toRideDTO(Ride ride);

    List<RideDTO> toRideDTO(List<Ride> rides);

    Ride toRide(RideDTO dto);

    @Mapping(target = "startLatitude", source = "startLongitude")
    @Mapping(target = "destinationLatitude", source = "destinationLongitude")
    void updateRideFromDTO(RideDTO dto, @MappingTarget Ride ride);
}