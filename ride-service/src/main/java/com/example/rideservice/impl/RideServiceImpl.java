package com.example.rideservice.impl;

import com.example.driver.dto.DriverDTO;
import com.example.driver.impl.DriverServiceImpl;
import com.example.rideservice.dto.RideDTO;
import com.example.rideservice.entity.Ride;
import com.example.rideservice.mapper.RideMapper;
import com.example.rideservice.repo.RideRepository;
import com.example.rideservice.service.RideService;
import com.example.rideservice.status.RideStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final DriverServiceImpl driverServiceImpl;

    @Override
    public RideDTO createRideWithDriver(Integer passengerId, Double startLatitude, Double startLongitude, Double destinationLatitude, Double destinationLongitude) {
        RideDTO rideDTO = new RideDTO();
        rideDTO.setPassengerId(passengerId);
        rideDTO.setStartLatitude(startLatitude);
        rideDTO.setStartLongitude(startLongitude);
        rideDTO.setDestinationLatitude(destinationLatitude);
        rideDTO.setDestinationLongitude(destinationLongitude);

        List<DriverDTO> availableDrivers = driverServiceImpl.findAvailableDrivers();

        if (!availableDrivers.isEmpty()) {
            DriverDTO firstAvailableDriver = availableDrivers.get(0);
            rideDTO.setDriverId(firstAvailableDriver.getId());
        } else {
        }

        RideDTO createdRide = rideMapper.toRideDTO(rideRepository.save(rideMapper.toRide(rideDTO)));

        return createdRide;
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
    public RideDTO acceptRide(Integer rideId) {
        Optional<Ride> rideOptional = rideRepository.findById(rideId);
        if (rideOptional.isPresent()) {
            Ride ride = rideOptional.get();
            ride.setStatus(RideStatus.ACCEPTED);

            Ride updatedRide = rideRepository.save(ride);
            return rideMapper.toRideDTO(updatedRide);
        } else {
            return null;
        }
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