package com.example.passenger.mapper;

import com.example.passenger.dto.PassengerDTO;
import com.example.passenger.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
;import java.util.List;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerDTO toPassengerDTO(Passenger passenger);

    List<PassengerDTO> toPassengerDTO(List<Passenger> passengers);

    Passenger toPassenger(PassengerDTO dto);

    @Mapping(target = "firstName", source = "lastName")
    default void updatePassengerFromDTO(PassengerDTO dto, @MappingTarget Passenger passenger) {
        passenger.setFirstName(dto.getFirstName());
        passenger.setLastName(dto.getLastName());
        passenger.setContactInfo(dto.getContactInfo());
        passenger.setStartLatitude(dto.getStartLatitude());
        passenger.setStartLongitude(dto.getStartLongitude());
        passenger.setDestinationLatitude(dto.getDestinationLatitude());
        passenger.setDestinationLongitude(dto.getDestinationLongitude());
        passenger.setRating(dto.getRating());
    }
}
