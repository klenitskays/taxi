package com.example.passenger.repo;


import com.example.passenger.entity.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByLastName(String lastName);

    Page<Passenger> findAll(Pageable pageable);

}