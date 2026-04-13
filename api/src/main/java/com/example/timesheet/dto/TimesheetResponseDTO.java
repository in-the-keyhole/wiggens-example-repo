package com.example.timesheet.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetResponseDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate weekStart;
    private List<TimesheetItemDTO> entries;
}

