package com.example.timesheet.controller;

import com.example.timesheet.dto.SummaryReportDTO;
import com.example.timesheet.dto.TimesheetRequestDTO;
import com.example.timesheet.dto.TimesheetResponseDTO;
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
public class ApiV1Controller {
    private final TimesheetService timesheetService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/timesheets")
    public ResponseEntity<TimesheetResponseDTO> upsert(@Valid @RequestBody TimesheetRequestDTO request) {
        return ResponseEntity.ok(timesheetService.upsertTimesheet(request));
    }

    @GetMapping("/timesheets")
    public ResponseEntity<TimesheetResponseDTO> getTimesheet(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return ResponseEntity.ok(timesheetService.getTimesheet(employeeId, weekStart));
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<SummaryReportDTO> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(timesheetService.getSummary(from, to));
    }
}

