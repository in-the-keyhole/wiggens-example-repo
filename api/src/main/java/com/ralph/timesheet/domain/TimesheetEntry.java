package com.ralph.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Entity
@Table(name = "timesheet_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "timesheet_id")
    @ToString.Exclude
    private Timesheet timesheet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private BigDecimal hours;

    @Column(nullable = false)
    private String project;
}

