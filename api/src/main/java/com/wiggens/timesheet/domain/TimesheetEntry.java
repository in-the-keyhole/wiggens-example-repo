package com.wiggens.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private Timesheet timesheet;

    @Column(nullable = false)
    private int dayOfWeek; // 1..7 (Mon..Sun)

    @Column(nullable = false)
    private double hours; // 0..24

    private String project;
    private String notes;
}

