package com.wiggens.timesheet.service;

import com.wiggens.timesheet.domain.Employee;
import com.wiggens.timesheet.domain.Timesheet;
import com.wiggens.timesheet.domain.TimesheetEntry;
import com.wiggens.timesheet.dto.TimesheetDto;
import com.wiggens.timesheet.dto.TimesheetEntryDto;
import com.wiggens.timesheet.repository.EmployeeRepository;
import com.wiggens.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;

    public List<TimesheetDto> list() {
        return timesheetRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public TimesheetDto create(TimesheetDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NoSuchElementException("Employee not found"));

        Timesheet t = new Timesheet();
        t.setEmployee(employee);
        t.setWeekStart(dto.getWeekStart());
        if (dto.getEntries() != null) {
            for (TimesheetEntryDto ed : dto.getEntries()) {
                TimesheetEntry e = new TimesheetEntry();
                e.setTimesheet(t);
                e.setDayOfWeek(ed.getDayOfWeek());
                e.setHours(ed.getHours());
                e.setProject(ed.getProject());
                e.setNotes(ed.getNotes());
                t.getEntries().add(e);
            }
        }
        t = timesheetRepository.save(t);
        return toDto(t);
    }

    public TimesheetDto get(Long id) {
        return toDto(timesheetRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Timesheet not found")));
    }

    public TimesheetDto update(Long id, TimesheetDto dto) {
        Timesheet t = timesheetRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Timesheet not found"));
        if (dto.getWeekStart() != null) t.setWeekStart(dto.getWeekStart());
        t.getEntries().clear();
        if (dto.getEntries() != null) {
            for (TimesheetEntryDto ed : dto.getEntries()) {
                TimesheetEntry e = new TimesheetEntry();
                e.setTimesheet(t);
                e.setDayOfWeek(ed.getDayOfWeek());
                e.setHours(ed.getHours());
                e.setProject(ed.getProject());
                e.setNotes(ed.getNotes());
                t.getEntries().add(e);
            }
        }
        return toDto(timesheetRepository.save(t));
    }

    public double totalHoursForWeek(LocalDate weekStart) {
        LocalDate end = weekStart.plusDays(6);
        return timesheetRepository.findByWeekStartBetween(weekStart, end).stream()
                .flatMap(t -> t.getEntries().stream())
                .mapToDouble(TimesheetEntry::getHours)
                .sum();
    }

    public double totalHoursAllTime() {
        return timesheetRepository.findAll().stream()
                .flatMap(t -> t.getEntries().stream())
                .mapToDouble(TimesheetEntry::getHours)
                .sum();
    }

    public TimesheetDto toDto(Timesheet t) {
        return TimesheetDto.builder()
                .id(t.getId())
                .employeeId(t.getEmployee().getId())
                .weekStart(t.getWeekStart())
                .entries(t.getEntries().stream().map(e -> TimesheetEntryDto.builder()
                        .id(e.getId())
                        .dayOfWeek(e.getDayOfWeek())
                        .hours(e.getHours())
                        .project(e.getProject())
                        .notes(e.getNotes())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    public static LocalDate weekStartFor(LocalDate date) {
        LocalDate d = date;
        while (d.getDayOfWeek() != DayOfWeek.MONDAY) {
            d = d.minusDays(1);
        }
        return d;
    }
}

