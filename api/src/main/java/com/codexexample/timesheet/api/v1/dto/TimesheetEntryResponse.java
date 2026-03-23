package com.codexexample.timesheet.api.v1.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class TimesheetEntryResponse {
    UUID id;
    UUID projectId;
    String projectCode;
    String projectName;
    LocalDate workDate;
    BigDecimal hours;
    String notes;
}
