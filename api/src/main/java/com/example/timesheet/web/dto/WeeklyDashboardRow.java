package com.example.timesheet.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklyDashboardRow {
    private Long employeeId;
    private String employeeName;
    private double totalHours;
}

