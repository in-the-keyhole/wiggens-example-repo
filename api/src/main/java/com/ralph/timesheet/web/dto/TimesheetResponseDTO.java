package com.ralph.timesheet.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetResponseDTO {
    private Long id;
    private Long employeeId;
    private LocalDate weekStart;
    private BigDecimal mon;
    private BigDecimal tue;
    private BigDecimal wed;
    private BigDecimal thu;
    private BigDecimal fri;
    private BigDecimal sat;
    private BigDecimal sun;
    private BigDecimal total;
}

