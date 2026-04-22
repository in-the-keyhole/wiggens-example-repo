package com.wiggens.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "timesheets", uniqueConstraints = @UniqueConstraint(name = "uk_emp_week", columnNames = {"employee_id", "week_start"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart; // Monday of the week

    @Column(nullable = false)
    private double hoursMon;
    @Column(nullable = false)
    private double hoursTue;
    @Column(nullable = false)
    private double hoursWed;
    @Column(nullable = false)
    private double hoursThu;
    @Column(nullable = false)
    private double hoursFri;
    @Column(nullable = false)
    private double hoursSat;
    @Column(nullable = false)
    private double hoursSun;
}

