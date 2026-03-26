package com.example.timesheet.web;

import com.example.timesheet.dto.*;
import com.example.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/codex-example/api/v1")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService service;

    @PostMapping("/timesheets")
    public ResponseEntity<TimesheetResponse> upsert(@Valid @RequestBody TimesheetRequest request) {
        return ResponseEntity.ok(service.upsertTimesheet(request));
    }

    @GetMapping("/timesheets")
    public ResponseEntity<?> get(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        return service.getTimesheet(employeeId, weekStart)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/dashboard/summary")
    public WeeklySummaryResponse summary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return service.weeklySummary(weekStart);
    }
}

