package com.ralph.timesheet.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TimesheetResponseDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate weekStart;
    private List<TimesheetEntryDTO> entries;
    private BigDecimal totalHours;
}

