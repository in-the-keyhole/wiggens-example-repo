package com.wiggens.timesheet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimesheetEntryDto {
    private Long id;
    @Min(1)
    @Max(7)
    private int dayOfWeek; // 1..7 Mon..Sun
    @Min(0)
    @Max(24)
    private double hours;
    private String project;
    private String notes;
}

