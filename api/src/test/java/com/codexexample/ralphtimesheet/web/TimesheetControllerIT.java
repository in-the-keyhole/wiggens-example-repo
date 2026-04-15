package com.codexexample.ralphtimesheet.web;

import com.codexexample.ralphtimesheet.TimesheetApiApplication;
import com.codexexample.ralphtimesheet.dto.EmployeeDto;
import com.codexexample.ralphtimesheet.dto.TimesheetDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TimesheetApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimesheetControllerIT {

    @LocalServerPort
    int port;
    @Autowired
    TestRestTemplate rest;

    private String url(String path) {
        return "http://localhost:" + port + "/codex-example/api/v1" + path;
    }

    @Test
    void createEmployee_and_upsert_and_get_timesheet() {
        // create employee
        Map<String, Object> createReq = Map.of("name", "Alice", "email", "alice@example.com");
        ResponseEntity<EmployeeDto> empRes = rest.postForEntity(url("/employees"), createReq, EmployeeDto.class);
        assertThat(empRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long empId = empRes.getBody().getId();

        // upsert timesheet
        LocalDate monday = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        Map<String, Object> upsertReq = Map.of(
                "employeeId", empId,
                "weekStart", monday.toString(),
                "mon", 8, "tue", 8, "wed", 8, "thu", 8, "fri", 6, "sat", 0, "sun", 0,
                "notes", "Week 1"
        );
        ResponseEntity<TimesheetDto> upsertRes = rest.postForEntity(url("/timesheets"), upsertReq, TimesheetDto.class);
        assertThat(upsertRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(upsertRes.getBody().getTotal()).isEqualTo(38);

        // get it back
        ResponseEntity<TimesheetDto> getRes = rest.getForEntity(url("/timesheets?employeeId="+empId+"&weekStart="+monday), TimesheetDto.class);
        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRes.getBody().getFri()).isEqualTo(6);
    }
}

