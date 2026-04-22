package com.wiggens.timesheet.web;

import com.wiggens.timesheet.dto.TimesheetDTO;
import com.wiggens.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/codex-example/api/v1/timesheets")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    public ResponseEntity<TimesheetDTO> createOrUpdate(@Valid @RequestBody TimesheetDTO dto) {
        return ResponseEntity.ok(timesheetService.createOrUpdate(dto));
    }

    @GetMapping("/employee/{employeeId}")
    public List<TimesheetDTO> getByEmployee(@PathVariable Long employeeId) {
        return timesheetService.findByEmployee(employeeId);
    }

    @GetMapping("/employee/{employeeId}/week")
    public ResponseEntity<TimesheetDTO> getByEmployeeAndWeek(@PathVariable Long employeeId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return timesheetService.findByEmployeeAndWeek(employeeId, weekStart)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

