package com.example.timesheet.service;

import com.example.timesheet.domain.*;
import com.example.timesheet.repo.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;

    @Transactional
    public Timesheet upsertWeekly(Employee employee, LocalDate weekStart, List<TimesheetEntry> newEntries) {
        Timesheet ts = timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElseGet(() -> Timesheet.builder().employee(employee).weekStart(weekStart).build());
        // replace entries
        ts.getEntries().clear();
        for (TimesheetEntry e : newEntries) {
            e.setTimesheet(ts);
            ts.getEntries().add(e);
        }
        return timesheetRepository.save(ts);
    }

    public Timesheet getByEmployeeAndWeek(Employee employee, LocalDate weekStart) {
        return timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElse(null);
    }

    public double totalHours(Timesheet ts) {
        return ts.getEntries().stream().mapToDouble(TimesheetEntry::getHours).sum();
    }
}

