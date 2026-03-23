package com.codexexample.timesheet.api.v1.controller;

import com.codexexample.timesheet.api.v1.dto.TimesheetRequest;
import com.codexexample.timesheet.api.v1.dto.TimesheetResponse;
import com.codexexample.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/codex-example/api/v1/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService timesheetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetResponse createTimesheet(@Valid @RequestBody TimesheetRequest request) {
        return timesheetService.createTimesheet(request);
    }

    @GetMapping("/{id}")
    public TimesheetResponse getTimesheet(@PathVariable UUID id) {
        return timesheetService.getTimesheet(id);
    }

    @GetMapping
    public List<TimesheetResponse> getEmployeeTimesheets(@RequestParam UUID employeeId) {
        return timesheetService.getTimesheetsForEmployee(employeeId);
    }

    @PutMapping("/{id}")
    public TimesheetResponse updateTimesheet(@PathVariable UUID id,
                                             @Valid @RequestBody TimesheetRequest request) {
        return timesheetService.updateTimesheet(id, request);
    }
}
