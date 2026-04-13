package com.example.timesheet.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryReportDTO {
    // employeeName -> totalHours
    private Map<String, Double> totalsByEmployee;
}

