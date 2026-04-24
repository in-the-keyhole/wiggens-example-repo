package com.ralph.timesheet.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardMetricsDTO {
    private long employeesCount;
    private BigDecimal totalHoursThisWeek;
    private BigDecimal totalHoursAllTime;
}

