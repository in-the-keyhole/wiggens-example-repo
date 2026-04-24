package com.ralph.timesheet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TimesheetRequestDTO {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate weekStart;
    @NotEmpty
    @Valid
    private List<TimesheetEntryDTO> entries;
}

