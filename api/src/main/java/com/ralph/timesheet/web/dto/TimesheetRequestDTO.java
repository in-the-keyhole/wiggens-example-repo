package com.ralph.timesheet.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
public class TimesheetRequestDTO {
    @NotNull
    private Long employeeId;

    @NotNull
    private LocalDate weekStart; // ISO yyyy-MM-dd, Monday

    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal mon;
    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal tue;
    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal wed;
    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal thu;
    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal fri;
    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal sat;
    @NotNull @DecimalMin("0.0") @DecimalMax("24.0")
    private BigDecimal sun;
}

