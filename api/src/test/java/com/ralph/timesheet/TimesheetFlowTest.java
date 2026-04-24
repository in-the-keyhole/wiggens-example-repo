package com.ralph.timesheet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralph.timesheet.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TimesheetFlowTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createTimesheetAndReport() throws Exception {
        // Create employee
        EmployeeDTO empReq = EmployeeDTO.builder().fullName("Alice Wonderland").email("alice@example.com").build();
        String empJson = objectMapper.writeValueAsString(empReq);
        String empRespJson = mockMvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        EmployeeDTO employee = objectMapper.readValue(empRespJson, EmployeeDTO.class);

        // Upsert timesheet
        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        TimesheetRequestDTO tsReq = TimesheetRequestDTO.builder()
                .employeeId(employee.getId())
                .weekStart(weekStart)
                .entries(List.of(
                        TimesheetEntryDTO.builder().date(weekStart).project("Project X").hours(new BigDecimal("8.0")).build(),
                        TimesheetEntryDTO.builder().date(weekStart.plusDays(1)).project("Project X").hours(new BigDecimal("7.5")).build()
                ))
                .build();
        String tsRespJson = mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tsReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TimesheetResponseDTO tsResp = objectMapper.readValue(tsRespJson, TimesheetResponseDTO.class);
        assertThat(tsResp.getTotalHours()).isEqualByComparingTo(new BigDecimal("15.5"));

        // Fetch report
        String reportJson = mockMvc.perform(get("/codex-example/api/v1/reports/summary")
                        .param("from", weekStart.toString())
                        .param("to", weekStart.plusDays(6).toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<SummaryReportDTO> report = objectMapper.readValue(reportJson, new TypeReference<>(){});
        assertThat(report).isNotEmpty();
        assertThat(report.get(0).getTotalHours()).isEqualByComparingTo(new BigDecimal("15.5"));
    }
}

