package com.wiggens.timesheet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TimesheetDto {
    private Long id;
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate weekStart;
    @Valid
    private List<TimesheetEntryDto> entries;
}

