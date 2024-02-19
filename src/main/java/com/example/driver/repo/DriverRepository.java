package com.example.driver.repo;

import com.example.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    // Методы для работы с базой данных
}
