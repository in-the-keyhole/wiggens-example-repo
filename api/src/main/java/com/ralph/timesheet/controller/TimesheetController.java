package com.ralph.timesheet.controller;

import com.ralph.timesheet.dto.TimesheetDtos;
import com.ralph.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/codex-example/api/v1")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping("/timesheets")
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetDtos.TimesheetResponse create(@Valid @RequestBody TimesheetDtos.CreateTimesheetRequest req) {
        return timesheetService.create(req);
    }

    @PutMapping("/timesheets/{id}")
    public TimesheetDtos.TimesheetResponse update(@PathVariable Long id,
                                                  @Valid @RequestBody TimesheetDtos.UpdateTimesheetRequest req) {
        return timesheetService.update(id, req);
    }

    @GetMapping("/reports/summary")
    public List<TimesheetDtos.SummaryResponse> summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return timesheetService.summary(start, end);
    }

    @GetMapping("/employees/{employeeId}/timesheets")
    public List<TimesheetDtos.TimesheetResponse> listByEmployee(@PathVariable Long employeeId) {
        return timesheetService.listByEmployee(employeeId);
    }
}
