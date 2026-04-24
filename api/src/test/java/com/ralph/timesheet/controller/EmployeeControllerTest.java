package com.ralph.timesheet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralph.timesheet.dto.EmployeeDTO;
import com.ralph.timesheet.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EmployeeService employeeService;

    @Test
    void createEmployee() throws Exception {
        EmployeeDTO req = EmployeeDTO.builder().fullName("Jane Doe").email("jane@example.com").build();
        EmployeeDTO resp = EmployeeDTO.builder().id(1L).fullName("Jane Doe").email("jane@example.com").build();
        Mockito.when(employeeService.create(Mockito.any())).thenReturn(resp);

        mockMvc.perform(post("/codex-example/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void listEmployees() throws Exception {
        List<EmployeeDTO> list = List.of(
                EmployeeDTO.builder().id(1L).fullName("Jane Doe").email("jane@example.com").build(),
                EmployeeDTO.builder().id(2L).fullName("John Smith").email("john@example.com").build()
        );
        Mockito.when(employeeService.list()).thenReturn(list);

        mockMvc.perform(get("/codex-example/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}

