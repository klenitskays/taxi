package com.example.driver.request;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;

public class DriverApiExample {
    public static void runExample() {
        // Создаем экземпляр DriverApiClient с базовым URL API
        String baseUrl = "http://localhost:8081"; // Замените на фактический URL API
        DriverApiClient apiClient = new DriverApiClient(baseUrl);

        // Пример использования методов
        DriverDTO newDriverDTO = new DriverDTO();
        newDriverDTO.setFirstName("John");
        newDriverDTO.setLastName("Doe");

        // Создание водителя
        Driver createdDriver = apiClient.createDriver(newDriverDTO);
        System.out.println("Created driver: " + createdDriver);

        // Получение водителя по ID
        Long driverId = (long) createdDriver.getId();
        Driver retrievedDriver = apiClient.getDriverById(driverId);
        System.out.println("Retrieved driver: " + retrievedDriver);

        // Обновление водителя
        DriverDTO updatedDriverDTO = new DriverDTO();
        updatedDriverDTO.setFirstName("Jane");
        updatedDriverDTO.setLastName("Doe");
        apiClient.updateDriver(driverId, updatedDriverDTO);
        System.out.println("Driver updated successfully.");

        // Удаление водителя
        apiClient.deleteDriver(driverId);
        System.out.println("Driver deleted successfully.");
    }

    public static void main(String[] args) {
        runExample();
    }
}