package com.example.timesheet.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TimesheetDTO {
    private Long id;

    @NotBlank
    private String employeeId;

    @NotNull
    private LocalDate weekStart; // Monday of the week

    @Valid
    private List<TimesheetEntryDTO> entries;
}

