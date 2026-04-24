package com.ralph.timesheet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TimesheetEntry> entries = new ArrayList<>();

    public void setEntries(List<TimesheetEntry> entries) {
        this.entries.clear();
        if (entries != null) {
            entries.forEach(this::addEntry);
        }
    }

    public void addEntry(TimesheetEntry entry) {
        entry.setTimesheet(this);
        this.entries.add(entry);
    }
}

