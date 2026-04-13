package com.example.timesheet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetItemDTO {
    @NotNull
    private LocalDate workDate;

    @NotBlank
    private String project;

    @Positive
    @Max(24)
    private double hours;

    private String note;
}

