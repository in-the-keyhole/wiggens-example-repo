package com.wiggens.timesheet.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiggens.timesheet.dto.EmployeeDTO;
import com.wiggens.timesheet.dto.TimesheetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TimesheetControllerTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void upsertAndGetTimesheet() throws Exception {
        // create employee first
        EmployeeDTO emp = EmployeeDTO.builder().name("Bob").email("bob@example.com").build();
        String loc = mvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        String empJson = mvc.perform(get(loc)).andReturn().getResponse().getContentAsString();
        EmployeeDTO saved = objectMapper.readValue(empJson, EmployeeDTO.class);

        TimesheetDTO ts = TimesheetDTO.builder()
                .employeeId(saved.getId())
                .weekStart(LocalDate.now().with(java.time.DayOfWeek.MONDAY))
                .hoursMon(8).hoursTue(8).hoursWed(8).hoursThu(8).hoursFri(6).hoursSat(0).hoursSun(0)
                .build();

        mvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ts)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(saved.getId()))
                .andExpect(jsonPath("$.hoursFri").value(6.0));

        mvc.perform(get("/codex-example/api/v1/timesheets/employee/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employeeId").value(saved.getId()));
    }
}

