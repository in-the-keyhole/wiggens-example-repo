package com.example.timesheet.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TimesheetResponse {
    private Long id;
    private Long employeeId;
    private LocalDate weekStart;
    private double totalHours;
    private List<TimesheetEntryView> entries;
}

