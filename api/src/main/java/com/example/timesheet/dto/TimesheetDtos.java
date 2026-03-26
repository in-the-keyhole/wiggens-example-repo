package com.example.timesheet.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate weekStart;
    @Size(min = 1, max = 7)
    private List<Entry> entries;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Entry {
        @Min(1)
        @Max(7)
        private int dayOfWeek;
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "24.0")
        private BigDecimal hours;
        private String project;
        private String notes;
    }
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetEntryDto {
    private int dayOfWeek;
    private BigDecimal hours;
    private String project;
    private String notes;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetResponse {
    private Long id;
    private Long employeeId;
    private LocalDate weekStart;
    private List<TimesheetEntryDto> entries;
    private BigDecimal totalHours;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklySummaryItem {
    private Long employeeId;
    private String employeeName;
    private BigDecimal totalHours;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklySummaryResponse {
    private LocalDate weekStart;
    private List<WeeklySummaryItem> items;
}
