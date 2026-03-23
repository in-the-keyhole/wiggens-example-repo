package com.codexexample.timesheet.api.v1.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class TimesheetEntryRequest {
    @NotNull(message = "Project id is required")
    UUID projectId;

    @NotNull(message = "Work date is required")
    LocalDate workDate;

    @NotNull(message = "Hours are required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Hours must be greater than 0")
    @DecimalMax(value = "24.0", message = "Hours must be less than or equal to 24")
    BigDecimal hours;

    String notes;
}
