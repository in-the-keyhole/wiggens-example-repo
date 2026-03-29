package com.example.timesheet.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class TimesheetEntryDto {
    @NotNull
    private Long projectId;
    @NotNull
    private DayOfWeek dayOfWeek;
    @PositiveOrZero
    @DecimalMax("24.0")
    private double hours;
}
