package com.example.timesheet;

import com.example.timesheet.dto.EmployeeDto;
import com.example.timesheet.dto.TimesheetDto;
import com.example.timesheet.dto.TimesheetEntryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimesheetApiApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void endToEnd_employeeAndTimesheetFlow() {
        EmployeeDto newEmp = EmployeeDto.builder()
                .firstName("Ralph")
                .lastName("Wiggum")
                .email("ralph@example.com")
                .build();
        ResponseEntity<EmployeeDto> createdEmp = rest.postForEntity("/codex-example/api/v1/employees", newEmp, EmployeeDto.class);
        assertThat(createdEmp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long empId = createdEmp.getBody().getId();
        assertThat(empId).isNotNull();

        TimesheetDto ts = TimesheetDto.builder()
                .employeeId(empId)
                .weekStart(LocalDate.now())
                .entries(List.of(
                        TimesheetEntryDto.builder().date(LocalDate.now()).hours(new BigDecimal("8.0")).build()
                ))
                .build();
        ResponseEntity<TimesheetDto> createdTs = rest.postForEntity("/codex-example/api/v1/timesheets", ts, TimesheetDto.class);
        assertThat(createdTs.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdTs.getBody().getId()).isNotNull();
    }
}

