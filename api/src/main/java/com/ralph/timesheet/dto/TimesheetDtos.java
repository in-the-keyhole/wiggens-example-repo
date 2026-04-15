package com.ralph.timesheet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class TimesheetDtos {

    @Data
    @Builder
    public static class CreateTimesheetRequest {
        @NotNull
        private Long employeeId;
        @NotNull
        private LocalDate weekStart;
        @Size(min = 1, max = 7)
        @Valid
        private List<EntryDto> entries;
    }

    @Data
    @Builder
    public static class UpdateTimesheetRequest {
        @Size(min = 1, max = 7)
        @Valid
        private List<EntryDto> entries;
    }

    @Data
    @Builder
    public static class EntryDto {
        @NotNull
        private DayOfWeek dayOfWeek;
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @DecimalMax(value = "24.0")
        private BigDecimal hours;
        @NotBlank
        private String project;
    }

    @Data
    @Builder
    public static class TimesheetResponse {
        private Long id;
        private Long employeeId;
        private LocalDate weekStart;
        private List<EntryDto> entries;
        private BigDecimal totalHours;
    }

    @Data
    @Builder
    public static class SummaryResponse {
        private Long employeeId;
        private String employeeName;
        private LocalDate weekStart;
        private BigDecimal totalHours;
    }
}

