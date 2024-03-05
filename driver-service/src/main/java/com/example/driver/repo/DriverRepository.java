package com.example.driver.repo;

import com.example.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByLastName(String lastName);
    Optional<Driver> findByAvailableIsTrue();
}