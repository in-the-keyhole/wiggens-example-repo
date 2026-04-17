package com.wiggens.timesheet.web;

import com.wiggens.timesheet.dto.EmployeeDto;
import com.wiggens.timesheet.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    EmployeeService employeeService;

    @Test
    void listEmployees_ok() throws Exception {
        when(employeeService.list()).thenReturn(List.of(
                EmployeeDto.builder().id(1L).firstName("Ralph").lastName("Wiggins").email("ralph@example.com").build()
        ));

        mvc.perform(get("/codex-example/api/v1/employees").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}

