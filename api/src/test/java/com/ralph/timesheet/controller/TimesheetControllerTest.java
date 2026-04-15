package com.ralph.timesheet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralph.timesheet.dto.EmployeeDto;
import com.ralph.timesheet.dto.TimesheetDtos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TimesheetControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createTimesheetAndSummary() throws Exception {
        EmployeeDto dto = EmployeeDto.builder().name("Bob").email("bob@example.com").build();
        String empJson = mockMvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long empId = objectMapper.readTree(empJson).get("id").asLong();

        TimesheetDtos.EntryDto e1 = TimesheetDtos.EntryDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .hours(new BigDecimal("8"))
                .project("Project A").build();
        TimesheetDtos.EntryDto e2 = TimesheetDtos.EntryDto.builder()
                .dayOfWeek(DayOfWeek.TUESDAY)
                .hours(new BigDecimal("8"))
                .project("Project A").build();

        TimesheetDtos.CreateTimesheetRequest tsReq = TimesheetDtos.CreateTimesheetRequest.builder()
                .employeeId(empId)
                .weekStart(LocalDate.now().with(DayOfWeek.MONDAY))
                .entries(List.of(e1, e2))
                .build();

        mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tsReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalHours").value(16));

        LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
        mockMvc.perform(get("/codex-example/api/v1/reports/summary")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(empId))
                .andExpect(jsonPath("$[0].totalHours").value(16));

        // Browse by employee should return created timesheet
        mockMvc.perform(get("/codex-example/api/v1/employees/{id}/timesheets", empId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(empId))
                .andExpect(jsonPath("$[0].weekStart").value(start.toString()))
                .andExpect(jsonPath("$[0].totalHours").value(16));
    }
}
