package com.example.timesheet.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;

@Data
@Builder
public class TimesheetEntryView {
    private Long projectId;
    private String projectName;
    private DayOfWeek dayOfWeek;
    private double hours;
}

