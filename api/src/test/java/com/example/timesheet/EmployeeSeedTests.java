package com.example.timesheet;

import com.example.timesheet.dto.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeSeedTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void seeds_john_doe_on_startup() {
        ResponseEntity<List<EmployeeDto>> resp = rest.exchange(
                "/codex-example/api/v1/employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmployeeDto>>() {}
        );

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody())
                .anySatisfy(e -> assertThat(e.getEmail()).isEqualTo("john.doe@example.com"));
    }
}

