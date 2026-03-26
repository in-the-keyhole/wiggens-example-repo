package com.example.timesheet.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TimesheetEntryDTO {
    @NotBlank
    private String projectCode;

    @NotBlank
    private String task;

    @NotNull
    @Min(0)
    @Max(24)
    private Integer hours;
}

