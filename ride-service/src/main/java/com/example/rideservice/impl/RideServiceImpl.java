package com.example.rideservice.impl;

import com.example.driver.client.DriverClient;
import com.example.driver.dto.DriverDto;
import com.example.rideservice.dto.RideDto;
import com.example.rideservice.entity.Ride;
import com.example.rideservice.mapper.RideMapper;
import com.example.rideservice.repo.RideRepository;
import com.example.rideservice.service.RideService;
import com.example.rideservice.status.RideStatus;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    @Override
    public RideDto createRide(@Valid RideDto dto) {
        Ride ride = rideMapper.toRide(dto);
        ride.setStatus(RideStatus.CREATED);
        ride.setStartTime(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);

        return rideMapper.toRideDto(savedRide);
    }
    @Override
    public Page<RideDto> getAllRides(Pageable pageable) {
        Page<Ride> ridePage = rideRepository.findAll(pageable);
        return ridePage.map(rideMapper::toRideDto);
    }
    @Override
    public RideDto getRideById(Integer id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        return rideOptional.map(rideMapper::toRideDto).orElse(null);
    }

    @Override
    public List<RideDto> getRideByPassengerId(Integer passengerId) {
        List<Ride> rides = rideRepository.findByPassengerId(passengerId);
        return rides.stream()
                .map(rideMapper::toRideDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideDto> getRideByDriverId(Integer driverId) {
        List<Ride> rides = rideRepository.findByDriverId(driverId);
        return rides.stream()
                .map(rideMapper::toRideDto)
                .collect(Collectors.toList());
    }

    @Override
    public RideDto updateRide(@Valid RideDto dto, Integer id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            rideMapper.updateRideFromDto(dto, ride);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDto(updatedRide);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRide(Integer id) {
        rideRepository.deleteById(id);
    }
    @Override
    public RideDto acceptRide(Long rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(Math.toIntExact(rideId));
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.ACCEPTED);

            Ride updatedRide = rideRepository.save(ride);
            RideDto updatedRideDto = rideMapper.toRideDto(updatedRide);
            return updatedRideDto;
        } else {
            return null;
        }
    }

    @Override
    public List<DriverDto> getAvailableDrivers() {
        DriverClient driverClient = Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder())
                .target(DriverClient.class, "http://localhost:8081");

        return driverClient.getAvailableDrivers();
    }

    @Override
    public void toggleDriverAvailability(Long driverId) {
        DriverClient driverClient = Feign.builder()
                .contract(new SpringMvcContract())
                .decoder(new JacksonDecoder())
                .target(DriverClient.class, "http://localhost:8081");

        driverClient.toggleDriverAvailability(driverId);
    }


    @Override
    public RideDto startRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.IN_PROGRESS);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDto(updatedRide);
        } else {
            return null;
        }
    }
    @Override
    public RideDto cancelRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.CANCELLED);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDto(updatedRide);
        } else {
            return null;
        }
    }

    @Override
    public RideDto completeRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.COMPLETED);
            ride.setEndTime(LocalDateTime.now());

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDto(updatedRide);
        } else {
            return null;
        }
    }
}