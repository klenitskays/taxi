package com.example.driver.repo;

import com.example.driver.entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByLastName(String lastName);
    List<Driver> findByAvailableIsTrue();
    Page<Driver> findAll(Pageable pageable);
}