package com.example.timesheet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetRequestDTO {
    @NotNull
    private Long employeeId;

    @NotNull
    private LocalDate weekStart; // Monday

    @NotEmpty
    @Valid
    private List<TimesheetItemDTO> entries;
}

