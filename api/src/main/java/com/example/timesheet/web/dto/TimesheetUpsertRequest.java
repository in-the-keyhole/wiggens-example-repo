package com.example.timesheet.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TimesheetUpsertRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate weekStart; // Monday
    @Valid
    @Size(min = 0, max = 200)
    private List<TimesheetEntryDto> entries;
}
