package com.example.timesheet.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TimesheetEntryDto {
    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private LocalDate workDate;

    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "24.0")
    private BigDecimal hours;

    @Size(max = 1024)
    private String description;
}

