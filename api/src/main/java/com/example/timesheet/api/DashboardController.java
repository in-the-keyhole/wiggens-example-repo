package com.example.timesheet.api;

import com.example.timesheet.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/codex-example/api/v1/dashboard")
public class DashboardController {
    private final TimesheetService service;

    public DashboardController(TimesheetService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getSummary(@RequestParam String weekStart) {
        LocalDate ws = LocalDate.parse(weekStart);
        int total = service.totalHoursForWeek(ws);
        Map<String, Object> payload = new HashMap<>();
        payload.put("weekStart", ws.toString());
        payload.put("totalHours", total);
        return ResponseEntity.ok(payload);
    }
}

