package com.example.driver.request;

import com.example.driver.dto.DriverDTO;
import com.example.driver.entity.Driver;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class DriverApiClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public DriverApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }

    public Driver createDriver(DriverDTO driverDTO) {
        String url = baseUrl + "/driver";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DriverDTO> requestEntity = new HttpEntity<>(driverDTO, headers);
        ResponseEntity<Driver> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Driver.class);
        return responseEntity.getBody();
    }

    public Driver getDriverById(Long id) {
        String url = baseUrl + "/driver/id/" + id;
        ResponseEntity<Driver> responseEntity = restTemplate.getForEntity(url, Driver.class);
        return responseEntity.getBody();
    }

    public void updateDriver(Long id, DriverDTO updatedDriverDTO) {
        String url = baseUrl + "/driver/id/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DriverDTO> requestEntity = new HttpEntity<>(updatedDriverDTO, headers);
        restTemplate.put(url, requestEntity);
    }

    public void deleteDriver(Long id) {
        String url = baseUrl + "/driver/id/" + id;
        restTemplate.delete(url);
    }
}