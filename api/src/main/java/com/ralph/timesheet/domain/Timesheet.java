package com.ralph.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timesheets", uniqueConstraints = {
        @UniqueConstraint(name = "uk_timesheet_emp_week", columnNames = {"employee_id", "week_start"})
})
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal mon;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal tue;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal wed;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal thu;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal fri;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal sat;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal sun;
}

