package com.ralph.timesheet.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralph.timesheet.dto.EmployeeDTO;
import com.ralph.timesheet.dto.TimesheetEntryDTO;
import com.ralph.timesheet.dto.TimesheetRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DashboardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void metricsReturnsCountsAndTotals() throws Exception {
        // Baseline metrics
        String baseJson = mockMvc.perform(get("/codex-example/api/v1/dashboard"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, Object> base = objectMapper.readValue(baseJson, new TypeReference<>(){});
        long baseEmployees = ((Number) base.get("employeesCount")).longValue();
        BigDecimal baseWeek = new BigDecimal(base.get("totalHoursThisWeek").toString());
        BigDecimal baseAll = new BigDecimal(base.get("totalHoursAllTime").toString());

        // Create one employee
        EmployeeDTO empReq = EmployeeDTO.builder().fullName("Bob Builder").email("bob@example.com").build();
        String empRespJson = mockMvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(empReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EmployeeDTO employee = objectMapper.readValue(empRespJson, EmployeeDTO.class);

        // Create a timesheet for current week with 2 entries totalling 15.5
        LocalDate monday = startOfWeek(LocalDate.now());
        TimesheetRequestDTO tsReq = TimesheetRequestDTO.builder()
                .employeeId(employee.getId())
                .weekStart(monday)
                .entries(java.util.List.of(
                        TimesheetEntryDTO.builder().date(monday).project("A").hours(new BigDecimal("8.0")).build(),
                        TimesheetEntryDTO.builder().date(monday.plusDays(1)).project("A").hours(new BigDecimal("7.5")).build()
                ))
                .build();
        mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tsReq)))
                .andExpect(status().isCreated());

        String dashJson = mockMvc.perform(get("/codex-example/api/v1/dashboard"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> body = objectMapper.readValue(dashJson, new TypeReference<>(){});
        assertThat(((Number) body.get("employeesCount")).longValue()).isEqualTo(baseEmployees + 1);
        assertThat(new BigDecimal(body.get("totalHoursThisWeek").toString()))
                .isEqualByComparingTo(baseWeek.add(new BigDecimal("15.5")));
        assertThat(new BigDecimal(body.get("totalHoursAllTime").toString()))
                .isEqualByComparingTo(baseAll.add(new BigDecimal("15.5")));
    }

    private LocalDate startOfWeek(LocalDate date) {
        LocalDate d = date;
        while (d.getDayOfWeek().getValue() != 1) { // Monday
            d = d.minusDays(1);
        }
        return d;
    }
}
