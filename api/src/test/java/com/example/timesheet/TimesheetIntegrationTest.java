package com.example.timesheet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TimesheetIntegrationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void timesheetFlow() throws Exception {
        // Create employee
        var empBody = Map.of(
                "name", "Alice Example",
                "email", "alice@example.com"
        );
        String empJson = mapper.writeValueAsString(empBody);
        var empResp = mvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(empJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var empNode = mapper.readTree(empResp);
        long employeeId = empNode.get("id").asLong();

        // Create project
        var projBody = Map.of(
                "code", "PRJ-001",
                "name", "Test Project"
        );
        var projResp = mvc.perform(post("/codex-example/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(projBody)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var projNode = mapper.readTree(projResp);
        long projectId = projNode.get("id").asLong();

        // Create timesheet
        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        var tsBody = Map.of(
                "employeeId", employeeId,
                "weekStart", weekStart.toString()
        );
        var tsResp = mvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(tsBody)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var tsNode = mapper.readTree(tsResp);
        long timesheetId = tsNode.get("id").asLong();

        // Add entry
        var entryBody = Map.of(
                "projectId", projectId,
                "workDate", weekStart.toString(),
                "hours", new BigDecimal("8.0"),
                "description", "Worked on feature A"
        );
        var entryResp = mvc.perform(post("/codex-example/api/v1/timesheets/" + timesheetId + "/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(entryBody)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        var entryNode = mapper.readTree(entryResp);
        assertThat(entryNode.get("id").asLong()).isPositive();

        // Report
        var reportResp = mvc.perform(get("/codex-example/api/v1/reports/employee/" + employeeId)
                        .param("weekStart", weekStart.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var reportNode = mapper.readTree(reportResp);
        assertThat(reportNode.get("PRJ-001").decimalValue()).isEqualByComparingTo(new BigDecimal("8.0"));
    }
}

