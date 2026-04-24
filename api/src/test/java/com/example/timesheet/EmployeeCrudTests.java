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
class EmployeeCrudTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void can_create_update_get_list_and_delete_employee() {
        EmployeeDto e = EmployeeDto.builder().firstName("Ralph").lastName("Wiggum").email("ralph2@example.com").build();
        EmployeeDto created = rest.postForEntity("/codex-example/api/v1/employees", e, EmployeeDto.class).getBody();
        assertThat(created.getId()).isNotNull();

        created.setFirstName("Chief");
        rest.put("/codex-example/api/v1/employees/" + created.getId(), created);

        EmployeeDto fetched = rest.getForEntity("/codex-example/api/v1/employees/" + created.getId(), EmployeeDto.class).getBody();
        assertThat(fetched.getFirstName()).isEqualTo("Chief");

        ResponseEntity<List<EmployeeDto>> list = rest.exchange(
                "/codex-example/api/v1/employees",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<EmployeeDto>>() {}
        );
        assertThat(list.getBody()).isNotEmpty();

        rest.delete("/codex-example/api/v1/employees/" + created.getId());
    }
}

