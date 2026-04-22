package com.wiggens.timesheet.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class TimesheetDTO {
    Long id;
    @NotNull(message = "employeeId is required")
    Long employeeId;
    @NotNull(message = "weekStart is required")
    LocalDate weekStart;

    @Min(0) @Max(24) double hoursMon;
    @Min(0) @Max(24) double hoursTue;
    @Min(0) @Max(24) double hoursWed;
    @Min(0) @Max(24) double hoursThu;
    @Min(0) @Max(24) double hoursFri;
    @Min(0) @Max(24) double hoursSat;
    @Min(0) @Max(24) double hoursSun;
}

