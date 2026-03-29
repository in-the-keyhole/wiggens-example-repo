package com.example.timesheet.web;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Timesheet;
import com.example.timesheet.domain.TimesheetEntry;
import com.example.timesheet.service.EmployeeService;
import com.example.timesheet.service.ProjectService;
import com.example.timesheet.service.TimesheetService;
import com.example.timesheet.web.dto.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codex-example/api/v1")
public class TimesheetController {
    private final TimesheetService timesheetService;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    public TimesheetController(TimesheetService timesheetService, EmployeeService employeeService, ProjectService projectService) {
        this.timesheetService = timesheetService;
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @PostMapping("/timesheets")
    public ResponseEntity<TimesheetResponse> upsert(@Validated @RequestBody TimesheetUpsertRequest request) {
        Employee employee = employeeService.get(request.getEmployeeId());
        List<TimesheetEntry> entries = request.getEntries().stream().map(e ->
                TimesheetEntry.builder()
                        .project(projectService.get(e.getProjectId()))
                        .dayOfWeek(e.getDayOfWeek())
                        .hours(e.getHours())
                        .build()).collect(Collectors.toList());
        Timesheet ts = timesheetService.upsertWeekly(employee, request.getWeekStart(), entries);
        return ResponseEntity.ok(toResponse(ts));
    }

    @GetMapping("/timesheets")
    public ResponseEntity<TimesheetResponse> get(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        Employee e = employeeService.get(employeeId);
        Timesheet ts = timesheetService.getByEmployeeAndWeek(e, weekStart);
        if (ts == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toResponse(ts));
    }

    @GetMapping("/dashboard/weekly")
    public List<WeeklyDashboardRow> weekly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        // naive: sum total hours per employee who has a timesheet that week
        // In a real impl, we'd query and aggregate. Here we reuse repository via service.
        // For simplicity, return all employees with their weekly total (0 if none)
        return employeeService.list().stream().map(e -> {
            Timesheet ts = timesheetService.getByEmployeeAndWeek(e, weekStart);
            double total = ts == null ? 0.0 : timesheetService.totalHours(ts);
            return WeeklyDashboardRow.builder()
                    .employeeId(e.getId())
                    .employeeName(e.getFirstName() + " " + e.getLastName())
                    .totalHours(total)
                    .build();
        }).collect(Collectors.toList());
    }

    private TimesheetResponse toResponse(com.example.timesheet.domain.Timesheet ts) {
        return TimesheetResponse.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .weekStart(ts.getWeekStart())
                .totalHours(timesheetService.totalHours(ts))
                .entries(ts.getEntries().stream().map(en -> TimesheetEntryView.builder()
                        .projectId(en.getProject().getId())
                        .projectName(en.getProject().getName())
                        .dayOfWeek(en.getDayOfWeek())
                        .hours(en.getHours())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
