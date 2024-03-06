package com.example.driver.repo;

import com.example.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByLastName(String lastName);
    Optional<Driver> findByAvailableIsTrue();
}