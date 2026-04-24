package com.ralph.timesheet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TimesheetEntryDTO {
    private Long id;
    @NotNull
    private LocalDate date;
    private String project;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal hours;
}

