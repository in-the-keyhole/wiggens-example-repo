package com.ralph.timesheet.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SummaryReportDTO {
    private Long employeeId;
    private String employeeName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal totalHours;
}

