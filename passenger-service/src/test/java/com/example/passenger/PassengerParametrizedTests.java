package com.example.passenger;
import com.example.passenger.controller.PassengerController;
import com.example.passenger.dto.PassengerDTO;
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
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setFirstName(firstName);
        passengerDTO.setLastName(lastName);
        when(passengerService.create(passengerDTO)).thenReturn(passengerDTO);

        ResponseEntity<PassengerDTO> response = passengerController.create(passengerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(passengerDTO, response.getBody());
    }

    @DisplayName("Test readByLastName method")
    @ParameterizedTest(name = "Test case #{index} - Last Name: {0}")
    @MethodSource("lastNameData")
    public void testReadByLastName(String lastName) {
        List<PassengerDTO> passengers = Arrays.asList(
                new PassengerDTO("John", lastName),
                new PassengerDTO("Jane", lastName)
        );
        when(passengerService.readByLastName(lastName)).thenReturn(passengers);

        ResponseEntity<List<PassengerDTO>> response = passengerController.readByLastName(lastName);

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
        List<PassengerDTO> passengers = Arrays.asList(
                new PassengerDTO("John", "Doe"),
                new PassengerDTO("Jane", "Smith")
        );
        Page<PassengerDTO> passengerPage = new PageImpl<>(passengers, pageable, passengers.size());
        when(passengerService.getAllPassengers(pageable)).thenReturn(passengerPage);

        ResponseEntity<Page<PassengerDTO>> response = passengerController.getAllPassengers(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(passengerPage, response.getBody());
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
