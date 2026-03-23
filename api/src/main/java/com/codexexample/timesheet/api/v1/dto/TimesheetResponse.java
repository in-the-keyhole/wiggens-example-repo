package com.codexexample.timesheet.api.v1.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class TimesheetResponse {
    UUID id;
    UUID employeeId;
    String employeeName;
    LocalDate weekStart;
    String status;
    @Singular
    List<TimesheetEntryResponse> entries;
}
