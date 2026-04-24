package com.example.timesheet.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StatsDto {
    private long employeeCount;
    private BigDecimal totalHoursThisWeek;
    private BigDecimal totalHoursAllTime;
}

