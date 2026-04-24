package com.example.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    private Timesheet timesheet;

    @Column(nullable = false)
    private LocalDate date; // specific day in the week

    @Column(nullable = false)
    private BigDecimal hours; // 0.0 to 24.0
}

