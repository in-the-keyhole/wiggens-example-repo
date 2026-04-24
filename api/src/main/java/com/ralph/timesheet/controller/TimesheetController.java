package com.ralph.timesheet.controller;

import com.ralph.timesheet.dto.TimesheetRequestDTO;
import com.ralph.timesheet.dto.TimesheetResponseDTO;
import com.ralph.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/codex-example/api/v1/timesheets")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetResponseDTO upsert(@Valid @RequestBody TimesheetRequestDTO req) {
        return timesheetService.upsertTimesheet(req);
    }

    @GetMapping("/{employeeId}")
    public TimesheetResponseDTO getForWeek(@PathVariable Long employeeId,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return timesheetService.getTimesheet(employeeId, weekStart);
    }
}

