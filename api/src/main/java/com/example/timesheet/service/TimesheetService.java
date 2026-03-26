package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Timesheet;
import com.example.timesheet.domain.TimesheetEntry;
import com.example.timesheet.dto.*;
import com.example.timesheet.repo.EmployeeRepository;
import com.example.timesheet.repo.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    @Transactional
    public TimesheetResponse upsertTimesheet(TimesheetRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + request.getEmployeeId()));

        Timesheet ts = timesheetRepository
                .findByEmployeeIdAndWeekStart(employee.getId(), request.getWeekStart())
                .orElseGet(() -> Timesheet.builder()
                        .employee(employee)
                        .weekStart(request.getWeekStart())
                        .build());

        List<TimesheetEntry> newEntries = request.getEntries() == null ? List.of() : request.getEntries().stream()
                .map(e -> TimesheetEntry.builder()
                        .dayOfWeek(e.getDayOfWeek())
                        .hours(e.getHours())
                        .project(e.getProject())
                        .notes(e.getNotes())
                        .build())
                .sorted(Comparator.comparingInt(TimesheetEntry::getDayOfWeek))
                .collect(Collectors.toList());

        ts.setEntries(newEntries);
        Timesheet saved = timesheetRepository.save(ts);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Optional<TimesheetResponse> getTimesheet(Long employeeId, LocalDate weekStart) {
        return timesheetRepository.findByEmployeeIdAndWeekStart(employeeId, weekStart).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public WeeklySummaryResponse weeklySummary(LocalDate weekStart) {
        List<Timesheet> all = timesheetRepository.findAll();
        List<WeeklySummaryItem> items = all.stream()
                .filter(t -> t.getWeekStart().equals(weekStart))
                .collect(Collectors.groupingBy(Timesheet::getEmployee))
                .entrySet().stream()
                .map(e -> WeeklySummaryItem.builder()
                        .employeeId(e.getKey().getId())
                        .employeeName(e.getKey().getName())
                        .totalHours(e.getValue().stream()
                                .flatMap(t -> t.getEntries().stream())
                                .map(TimesheetEntry::getHours)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build())
                .sorted(Comparator.comparing(WeeklySummaryItem::getEmployeeName))
                .collect(Collectors.toList());
        return WeeklySummaryResponse.builder().weekStart(weekStart).items(items).build();
    }

    private TimesheetResponse toResponse(Timesheet ts) {
        var entries = ts.getEntries().stream()
                .sorted(Comparator.comparingInt(TimesheetEntry::getDayOfWeek))
                .map(e -> TimesheetEntryDto.builder()
                        .dayOfWeek(e.getDayOfWeek())
                        .hours(e.getHours())
                        .project(e.getProject())
                        .notes(e.getNotes())
                        .build())
                .collect(Collectors.toList());
        BigDecimal total = entries.stream().map(TimesheetEntryDto::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return TimesheetResponse.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .weekStart(ts.getWeekStart())
                .entries(entries)
                .totalHours(total)
                .build();
    }
}

