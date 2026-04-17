package com.wiggens.timesheet.web;

import com.wiggens.timesheet.dto.TimesheetDto;
import com.wiggens.timesheet.service.TimesheetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimesheetController.class)
class TimesheetControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    TimesheetService timesheetService;

    @Test
    void listTimesheets_ok() throws Exception {
        when(timesheetService.list()).thenReturn(List.of(
                TimesheetDto.builder().id(1L).employeeId(1L).weekStart(LocalDate.now()).entries(List.of()).build()
        ));

        mvc.perform(get("/codex-example/api/v1/timesheets").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}

