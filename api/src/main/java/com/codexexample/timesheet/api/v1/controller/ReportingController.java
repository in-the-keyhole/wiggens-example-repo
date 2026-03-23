package com.codexexample.timesheet.api.v1.controller;

import com.codexexample.timesheet.api.v1.dto.EmployeeHoursReportResponse;
import com.codexexample.timesheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/codex-example/api/v1/reports")
@RequiredArgsConstructor
public class ReportingController {

    private final TimesheetService timesheetService;

    @GetMapping("/employee-hours")
    public EmployeeHoursReportResponse getEmployeeHours(@RequestParam UUID employeeId,
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        LocalDate start,
                                                        @RequestParam(required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        LocalDate end) {
        return timesheetService.getEmployeeHoursReport(employeeId, start, end);
    }
}
