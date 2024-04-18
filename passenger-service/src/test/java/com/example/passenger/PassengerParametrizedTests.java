package com.example.passenger;
import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDto;
import com.example.passenger.dto.PassengerDtoList;
import com.example.passenger.service.PassengerService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PassengerParametrizedTests {
    private PassengerService passengerService = mock(PassengerService.class);
    private KafkaProducer<String, String> producer = mock(KafkaProducer.class);
    private String topicName = "test-topic";
    private PassengerController passengerController = new PassengerController(passengerService, producer, topicName);

    @DisplayName("Test create method")
    @ParameterizedTest(name = "Test case #{index} - First Name: {0}, Last Name: {1}")
    @MethodSource("createPassengerData")
    public void testCreate(String firstName, String lastName) {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setFirstName(firstName);
        passengerDto.setLastName(lastName);
        when(passengerService.create(passengerDto)).thenReturn(passengerDto);

        ResponseEntity<PassengerDto> response = passengerController.create(passengerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(passengerDto, response.getBody());
    }

    @DisplayName("Test readByLastName method")
    @ParameterizedTest(name = "Test case #{index} - Last Name: {0}")
    @MethodSource("lastNameData")
    public void testReadByLastName(String lastName) {
        List<PassengerDto> passengers = Arrays.asList(
                new PassengerDto("John", lastName),
                new PassengerDto("Jane", lastName)
        );
        when(passengerService.readByLastName(lastName)).thenReturn(passengers);

        ResponseEntity<List<PassengerDto>> response = passengerController.readByLastName(lastName);

        if (!passengers.isEmpty()) {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(passengers, response.getBody());
        } else {
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @DisplayName("Test getAllPassengers method")
    @ParameterizedTest(name = "Test case #{index}")
    @MethodSource("pageableData")
    public void testGetAllPassengers(Pageable pageable) {
        List<PassengerDto> passengers = Arrays.asList(
                new PassengerDto("John", "Doe"),
                new PassengerDto("Jane", "Smith")
        );
        PassengerDtoList passengerDtoList = new PassengerDtoList(passengers);

        when(passengerService.getAllPassengers()).thenReturn(passengers);

        ResponseEntity<PassengerDtoList> response = passengerController.getAllPassengers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(passengerDtoList, response.getBody());
    }

    private static Stream<String[]> createPassengerData() {
        return Stream.of(
                new String[]{"John", "Doe"},
                new String[]{"Jane", "Smith"}
        );
    }

    private static Stream<String> lastNameData() {
        return Stream.of(
                "Smith",
                "Johnson"
        );
    }

    private static Stream<Pageable> pageableData() {
        return Stream.of(
                PageRequest.of(0, 10, Sort.by("lastName").ascending()),
                PageRequest.of(1, 20, Sort.by("firstName").descending())
        );
    }
}
