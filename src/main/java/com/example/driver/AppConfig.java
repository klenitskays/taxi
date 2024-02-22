package com.example.driver;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import com.example.driver.mapper.DriverMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.driver")
public class AppConfig {
    // другие конфигурационные настройки

    @Bean
    public DriverMapper driverMapper() {
        return new DriverMapper() {
            @Override
            public DriverDTO toDriverDTO(Driver driver) {
                return null;
            }

            @Override
            public Driver toDriver(DriverDTO dto) {
                return null;
            }

            @Override
            public void updateDriverFromDTO(DriverDTO dto, Driver driver) {

            }
        };
    }
}