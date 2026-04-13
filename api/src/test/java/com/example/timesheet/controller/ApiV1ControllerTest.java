package com.example.timesheet.controller;

import com.example.timesheet.TimesheetApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TimesheetApplication.class)
@AutoConfigureMockMvc
class ApiV1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void health_ok() throws Exception {
        mockMvc.perform(get("/codex-example/api/v1/health").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}

