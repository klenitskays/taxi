package com.example.passenger.repo;


import com.example.passenger.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.util.List;
@Component
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByLastName(String lastName);

}