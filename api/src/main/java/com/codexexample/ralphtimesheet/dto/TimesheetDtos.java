package com.codexexample.ralphtimesheet.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TimesheetDto {
    private Long id;
    private Long employeeId;
    private LocalDate weekStart;
    private double mon;
    private double tue;
    private double wed;
    private double thu;
    private double fri;
    private double sat;
    private double sun;
    private String notes;
    public double getTotal() { return mon+tue+wed+thu+fri+sat+sun; }
}

@Data
public class UpsertTimesheetRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate weekStart;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double mon;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double tue;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double wed;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double thu;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double fri;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double sat;
    @DecimalMin(value = "0.0") @DecimalMax(value = "24.0")
    private double sun;
    private String notes;
}

@Data
@Builder
public class SummaryRowDto {
    private Long employeeId;
    private String employeeName;
    private double totalHours;
}
