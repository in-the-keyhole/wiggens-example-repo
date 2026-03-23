package com.codexexample.timesheet.api.v1.controller;

import com.codexexample.timesheet.api.v1.dto.EmployeeRequest;
import com.codexexample.timesheet.api.v1.dto.ProjectRequest;
import com.codexexample.timesheet.api.v1.dto.TimesheetEntryRequest;
import com.codexexample.timesheet.api.v1.dto.TimesheetRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TimesheetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndFetchTimesheet() throws Exception {
        UUID employeeId = createEmployee();
        UUID projectId = createProject();

        LocalDate monday = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        TimesheetRequest request = TimesheetRequest.builder()
                .employeeId(employeeId)
                .weekStart(monday)
                .entry(TimesheetEntryRequest.builder()
                        .projectId(projectId)
                        .workDate(monday.plusDays(1))
                        .hours(new BigDecimal("8"))
                        .notes("Feature work")
                        .build())
                .build();

        String timesheetResponse = mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode createdNode = objectMapper.readTree(timesheetResponse);
        String timesheetId = createdNode.get("id").asText();

        String fetchResponse = mockMvc.perform(get("/codex-example/api/v1/timesheets/" + timesheetId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode fetched = objectMapper.readTree(fetchResponse);
        assertThat(fetched.get("entries")).hasSize(1);
        assertThat(fetched.get("employeeId").asText()).isEqualTo(employeeId.toString());
    }

    @Test
    void shouldRejectWeekStartNotMonday() throws Exception {
        UUID employeeId = createEmployee();
        UUID projectId = createProject();

        LocalDate tuesday = LocalDate.now().with(java.time.DayOfWeek.MONDAY).plusDays(1);
        TimesheetRequest request = TimesheetRequest.builder()
                .employeeId(employeeId)
                .weekStart(tuesday)
                .entry(TimesheetEntryRequest.builder()
                        .projectId(projectId)
                        .workDate(tuesday)
                        .hours(new BigDecimal("2.5"))
                        .build())
                .build();

        mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGenerateEmployeeHoursReport() throws Exception {
        UUID employeeId = createEmployee();
        UUID projectId = createProject();
        LocalDate monday = LocalDate.now().with(java.time.DayOfWeek.MONDAY);

        TimesheetRequest request = TimesheetRequest.builder()
                .employeeId(employeeId)
                .weekStart(monday)
                .entries(List.of(
                        TimesheetEntryRequest.builder()
                                .projectId(projectId)
                                .workDate(monday)
                                .hours(new BigDecimal("4"))
                                .notes("Planning")
                                .build(),
                        TimesheetEntryRequest.builder()
                                .projectId(projectId)
                                .workDate(monday.plusDays(1))
                                .hours(new BigDecimal("3.5"))
                                .notes("Development")
                                .build()))
                .build();

        mockMvc.perform(post("/codex-example/api/v1/timesheets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        String reportResponse = mockMvc.perform(get("/codex-example/api/v1/reports/employee-hours")
                        .param("employeeId", employeeId.toString())
                        .param("start", monday.toString())
                        .param("end", monday.plusDays(6).toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode report = objectMapper.readTree(reportResponse);
        assertThat(report.get("totalHours").decimalValue()).isEqualByComparingTo("7.50");
        assertThat(report.get("projectHours")).hasSize(1);
    }

    private UUID createEmployee() throws Exception {
        EmployeeRequest request = EmployeeRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email(UUID.randomUUID() + "@example.com")
                .active(true)
                .build();
        String response = mockMvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    private UUID createProject() throws Exception {
        ProjectRequest request = ProjectRequest.builder()
                .code("PRJ-" + UUID.randomUUID().toString().substring(0, 8))
                .name("Project")
                .description("Sample project")
                .build();
        String response = mockMvc.perform(post("/codex-example/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }
}
