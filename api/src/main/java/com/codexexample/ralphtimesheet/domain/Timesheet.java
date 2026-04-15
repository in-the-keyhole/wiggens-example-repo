package com.codexexample.ralphtimesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "timesheets", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_week", columnNames = {"employee_id", "week_start"})
})
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
    private double mon;
    @Column(nullable = false)
    private double tue;
    @Column(nullable = false)
    private double wed;
    @Column(nullable = false)
    private double thu;
    @Column(nullable = false)
    private double fri;
    @Column(nullable = false)
    private double sat;
    @Column(nullable = false)
    private double sun;

    private String notes;
}

