package com.example.timesheet.web;

import com.example.timesheet.service.TimesheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("codex-example/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final TimesheetService timesheetService;

    @GetMapping("/employee/{employeeId}")
    public Map<String, BigDecimal> weekly(@PathVariable Long employeeId, @RequestParam LocalDate weekStart) {
        return timesheetService.weeklyReport(employeeId, weekStart);
    }
}

