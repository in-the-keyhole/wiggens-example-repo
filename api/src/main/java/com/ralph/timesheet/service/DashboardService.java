package com.ralph.timesheet.service;

import com.ralph.timesheet.dto.DashboardMetricsDTO;
import com.ralph.timesheet.model.Timesheet;
import com.ralph.timesheet.model.TimesheetEntry;
import com.ralph.timesheet.repository.EmployeeRepository;
import com.ralph.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    @Transactional(readOnly = true)
    public DashboardMetricsDTO metrics() {
        long employees = employeeRepository.count();
        BigDecimal allTime = sumHoursBetween(null, null);
        LocalDate monday = startOfWeek(LocalDate.now());
        BigDecimal thisWeek = sumHoursBetween(monday, monday.plusDays(6));
        return DashboardMetricsDTO.builder()
                .employeesCount(employees)
                .totalHoursAllTime(allTime)
                .totalHoursThisWeek(thisWeek)
                .build();
    }

    private BigDecimal sumHoursBetween(LocalDate from, LocalDate to) {
        List<Timesheet> all = timesheetRepository.findAll();
        return all.stream()
                .flatMap(ts -> ts.getEntries().stream())
                .filter(e -> from == null || !e.getDate().isBefore(from))
                .filter(e -> to == null || !e.getDate().isAfter(to))
                .map(TimesheetEntry::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalDate startOfWeek(LocalDate date) {
        LocalDate d = date;
        while (d.getDayOfWeek() != DayOfWeek.MONDAY) {
            d = d.minusDays(1);
        }
        return d;
    }
}

