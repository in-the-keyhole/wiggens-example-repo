package com.codexexample.timesheet.api.v1.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class TimesheetRequest {
    @NotNull(message = "Employee id is required")
    UUID employeeId;

    @NotNull(message = "Week start date is required")
    LocalDate weekStart;

    @Valid
    @Singular
    @NotEmpty(message = "At least one entry is required")
    List<TimesheetEntryRequest> entries;
}
