package com.example.timesheet;

import com.example.timesheet.dto.EmployeeDto;
import com.example.timesheet.dto.TimesheetDto;
import com.example.timesheet.dto.TimesheetEntryDto;
import com.example.timesheet.dto.StatsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StatsIntegrationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void stats_endpoint_returns_aggregates() {
        EmployeeDto e = EmployeeDto.builder().firstName("Lisa").lastName("Simpson").email("lisa@example.com").build();
        EmployeeDto created = rest.postForEntity("/codex-example/api/v1/employees", e, EmployeeDto.class).getBody();
        TimesheetDto ts = TimesheetDto.builder()
                .employeeId(created.getId())
                .weekStart(LocalDate.now())
                .entries(List.of(TimesheetEntryDto.builder().date(LocalDate.now()).hours(new BigDecimal("4.0")).build()))
                .build();
        rest.postForEntity("/codex-example/api/v1/timesheets", ts, TimesheetDto.class);

        ResponseEntity<StatsDto> stats = rest.getForEntity("/codex-example/api/v1/timesheets/stats", StatsDto.class);
        assertThat(stats.getBody()).isNotNull();
        assertThat(stats.getBody().getEmployeeCount()).isGreaterThanOrEqualTo(1);
    }
}

