package com.ralph.timesheet.controller;

import com.ralph.timesheet.dto.DashboardMetricsDTO;
import com.ralph.timesheet.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/codex-example/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public DashboardMetricsDTO metrics() {
        return dashboardService.metrics();
    }
}

