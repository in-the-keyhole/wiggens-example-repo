package com.ralph.timesheet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO {
    private Long employeeId;
    private String employeeName;
    private BigDecimal totalHours;
}

