package com.example.timesheet.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TimesheetEntryDto {
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("24.0")
    private BigDecimal hours;
}

