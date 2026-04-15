package com.ralph.timesheet.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralph.timesheet.web.dto.EmployeeDTO;
import com.ralph.timesheet.web.dto.TimesheetRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createEmployee_thenSubmitAndFetchTimesheet() throws Exception {
        EmployeeDTO emp = EmployeeDTO.builder().name("Alice").email("alice@example.com").build();
        String empJson = objectMapper.writeValueAsString(emp);

        String empResp = mockMvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        EmployeeDTO created = objectMapper.readValue(empResp, EmployeeDTO.class);

        TimesheetRequestDTO ts = TimesheetRequestDTO.builder()
                .employeeId(created.getId())
                .weekStart(LocalDate.now().with(java.time.DayOfWeek.MONDAY))
                .mon(new BigDecimal("8"))
                .tue(new BigDecimal("8"))
                .wed(new BigDecimal("8"))
                .thu(new BigDecimal("8"))
                .fri(new BigDecimal("4"))
                .sat(new BigDecimal("0"))
                .sun(new BigDecimal("0"))
                .build();

        mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ts)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(36.0));

        mockMvc.perform(get("/codex-example/api/v1/timesheets")
                        .param("employeeId", String.valueOf(created.getId()))
                        .param("weekStart", ts.getWeekStart().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fri").value(4.0));
    }
}

