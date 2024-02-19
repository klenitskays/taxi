package com.example.driver.repo;

import com.example.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findAll();
    Optional<Driver> findById(int id);
    Driver save(Driver driver);
    void deleteById(int id);
    List<Driver> findByFirstName(String firstName);
    List<Driver> findByLastName(String lastName);
    List<Driver> findByFirstNameAndLastName(String firstName, String lastName);
}
