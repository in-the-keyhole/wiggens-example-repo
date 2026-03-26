package com.example.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "timesheet_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheet;

    @Column(nullable = false)
    private int dayOfWeek; // 1=Mon .. 7=Sun

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal hours;

    private String project;
    private String notes;
}

