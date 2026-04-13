package com.example.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "timesheet_entries")
@Data
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
    private LocalDate workDate;

    @Column(nullable = false)
    private String project;

    @Column(nullable = false)
    private double hours;

    private String note;
}

