package com.ralph.timesheet.service;

import com.ralph.timesheet.domain.Employee;
import com.ralph.timesheet.domain.Timesheet;
import com.ralph.timesheet.domain.TimesheetEntry;
import com.ralph.timesheet.dto.TimesheetDtos;
import com.ralph.timesheet.repository.EmployeeRepository;
import com.ralph.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    @Transactional
    public TimesheetDtos.TimesheetResponse create(TimesheetDtos.CreateTimesheetRequest req) {
        Employee employee = employeeRepository.findById(req.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Timesheet timesheet = timesheetRepository.findByEmployeeAndWeekStart(employee, req.getWeekStart())
                .orElse(Timesheet.builder().employee(employee).weekStart(req.getWeekStart()).build());

        timesheet.getEntries().clear();
        List<TimesheetEntry> entries = req.getEntries().stream()
                .map(e -> TimesheetEntry.builder()
                        .timesheet(timesheet)
                        .dayOfWeek(e.getDayOfWeek())
                        .hours(e.getHours())
                        .project(e.getProject())
                        .build())
                .collect(Collectors.toList());
        timesheet.getEntries().addAll(entries);

        Timesheet saved = timesheetRepository.save(timesheet);
        return toResponse(saved);
    }

    @Transactional
    public TimesheetDtos.TimesheetResponse update(Long id, TimesheetDtos.UpdateTimesheetRequest req) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));
        timesheet.getEntries().clear();
        List<TimesheetEntry> entries = req.getEntries().stream()
                .map(e -> TimesheetEntry.builder()
                        .timesheet(timesheet)
                        .dayOfWeek(e.getDayOfWeek())
                        .hours(e.getHours())
                        .project(e.getProject())
                        .build())
                .collect(Collectors.toList());
        timesheet.getEntries().addAll(entries);
        Timesheet saved = timesheetRepository.save(timesheet);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TimesheetDtos.SummaryResponse> summary(LocalDate start, LocalDate end) {
        return timesheetRepository.findByWeekStartBetween(start, end).stream()
                .map(ts -> TimesheetDtos.SummaryResponse.builder()
                        .employeeId(ts.getEmployee().getId())
                        .employeeName(ts.getEmployee().getName())
                        .weekStart(ts.getWeekStart())
                        .totalHours(ts.getEntries().stream()
                                .map(TimesheetEntry::getHours)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build())
                .collect(Collectors.toList());
    }

    private TimesheetDtos.TimesheetResponse toResponse(Timesheet ts) {
        return TimesheetDtos.TimesheetResponse.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .weekStart(ts.getWeekStart())
                .entries(ts.getEntries().stream()
                        .map(e -> TimesheetDtos.EntryDto.builder()
                                .dayOfWeek(e.getDayOfWeek())
                                .hours(e.getHours())
                                .project(e.getProject())
                                .build())
                        .collect(Collectors.toList()))
                .totalHours(ts.getEntries().stream()
                        .map(TimesheetEntry::getHours)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }
}

