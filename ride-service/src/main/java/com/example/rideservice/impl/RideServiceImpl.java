package com.example.rideservice.impl;

import com.example.driver.client.DriverClient;
import com.example.driver.dto.DriverDTO;
import com.example.rideservice.dto.RideDTO;
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
    public RideDTO createRide(@Valid RideDTO dto) {
        Ride ride = rideMapper.toRide(dto);
        ride.setStatus(RideStatus.CREATED);
        ride.setStartTime(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);

        return rideMapper.toRideDTO(savedRide);
    }
    @Override
    public Page<RideDTO> getAllRides(Pageable pageable) {
        Page<Ride> ridePage = rideRepository.findAll(pageable);
        return ridePage.map(rideMapper::toRideDTO);
    }
    @Override
    public RideDTO getRideById(Integer id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        return rideOptional.map(rideMapper::toRideDTO).orElse(null);
    }

    @Override
    public List<RideDTO> getRideByPassengerId(Integer passengerId) {
        List<Ride> rides = rideRepository.findByPassengerId(passengerId);
        return rides.stream()
                .map(rideMapper::toRideDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideDTO> getRideByDriverId(Integer driverId) {
        List<Ride> rides = rideRepository.findByDriverId(driverId);
        return rides.stream()
                .map(rideMapper::toRideDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RideDTO updateRide(@Valid RideDTO dto, Integer id) {
        Optional<Ride> rideOptional = rideRepository.findById(id);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            rideMapper.updateRideFromDTO(dto, ride);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDTO(updatedRide);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRide(Integer id) {
        rideRepository.deleteById(id);
    }
    @Override
    public RideDTO acceptRide(Long rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(Math.toIntExact(rideId));
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.ACCEPTED);

            Ride updatedRide = rideRepository.save(ride);
            RideDTO updatedRideDTO = rideMapper.toRideDTO(updatedRide);
            return updatedRideDTO;
        } else {
            return null;
        }
    }

    @Override
    public List<DriverDTO> getAvailableDrivers() {
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
    public RideDTO startRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.IN_PROGRESS);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDTO(updatedRide);
        } else {
            return null;
        }
    }
    @Override
    public RideDTO cancelRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.CANCELLED);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDTO(updatedRide);
        } else {
            return null;
        }
    }

    @Override
    public RideDTO completeRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.COMPLETED);
            ride.setEndTime(LocalDateTime.now());

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDTO(updatedRide);
        } else {
            return null;
        }
    }
}