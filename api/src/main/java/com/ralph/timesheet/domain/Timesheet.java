package com.ralph.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "timesheets",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id", "week_start"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TimesheetEntry> entries = new ArrayList<>();
}

