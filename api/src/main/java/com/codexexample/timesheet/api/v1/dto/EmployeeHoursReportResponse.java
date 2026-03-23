package com.codexexample.timesheet.api.v1.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class EmployeeHoursReportResponse {
    UUID employeeId;
    String employeeName;
    LocalDate startDate;
    LocalDate endDate;
    BigDecimal totalHours;
    @Singular
    List<ProjectHours> projectHours;

    @Value
    @Builder
    public static class ProjectHours {
        UUID projectId;
        String projectCode;
        String projectName;
        BigDecimal hours;
    }
}
