package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Project;
import com.example.timesheet.domain.Timesheet;
import com.example.timesheet.domain.TimesheetEntry;
import com.example.timesheet.dto.TimesheetDto;
import com.example.timesheet.dto.TimesheetEntryDto;
import com.example.timesheet.repository.TimesheetEntryRepository;
import com.example.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final TimesheetEntryRepository entryRepository;
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    @Transactional
    public TimesheetDto create(TimesheetDto dto) {
        Employee employee = employeeService.getEntity(dto.getEmployeeId());
        Timesheet ts = timesheetRepository.findByEmployeeAndWeekStart(employee, dto.getWeekStart())
                .orElseGet(() -> Timesheet.builder()
                        .employee(employee)
                        .weekStart(dto.getWeekStart())
                        .build());
        Timesheet saved = timesheetRepository.save(ts);
        return map(saved);
    }

    @Transactional(readOnly = true)
    public TimesheetDto get(Long id) {
        Timesheet ts = timesheetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));
        return map(ts);
    }

    @Transactional
    public TimesheetEntryDto addEntry(Long timesheetId, TimesheetEntryDto dto) {
        Timesheet ts = timesheetRepository.findById(timesheetId).orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));
        Project project = projectService.getEntity(dto.getProjectId());
        TimesheetEntry entry = TimesheetEntry.builder()
                .timesheet(ts)
                .project(project)
                .workDate(dto.getWorkDate())
                .hours(dto.getHours())
                .description(dto.getDescription())
                .build();
        TimesheetEntry saved = entryRepository.save(entry);
        return map(saved);
    }

    @Transactional
    public TimesheetEntryDto updateEntry(Long timesheetId, Long entryId, TimesheetEntryDto dto) {
        TimesheetEntry entry = entryRepository.findById(entryId).orElseThrow(() -> new IllegalArgumentException("Entry not found"));
        if (!entry.getTimesheet().getId().equals(timesheetId)) {
            throw new IllegalArgumentException("Entry does not belong to timesheet");
        }
        if (dto.getProjectId() != null) {
            entry.setProject(projectService.getEntity(dto.getProjectId()));
        }
        if (dto.getWorkDate() != null) {
            entry.setWorkDate(dto.getWorkDate());
        }
        if (dto.getHours() != null) {
            entry.setHours(dto.getHours());
        }
        entry.setDescription(dto.getDescription());
        return map(entryRepository.save(entry));
    }

    @Transactional
    public void deleteEntry(Long timesheetId, Long entryId) {
        TimesheetEntry entry = entryRepository.findById(entryId).orElseThrow(() -> new IllegalArgumentException("Entry not found"));
        if (!entry.getTimesheet().getId().equals(timesheetId)) {
            throw new IllegalArgumentException("Entry does not belong to timesheet");
        }
        entryRepository.delete(entry);
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> weeklyReport(Long employeeId, LocalDate weekStart) {
        Employee employee = employeeService.getEntity(employeeId);
        Timesheet ts = timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found for week"));
        return ts.getEntries().stream()
                .collect(Collectors.groupingBy(e -> e.getProject().getCode(),
                        Collectors.mapping(TimesheetEntry::getHours,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    private TimesheetDto map(Timesheet ts) {
        return TimesheetDto.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .weekStart(ts.getWeekStart())
                .entries(ts.getEntries() == null ? null : ts.getEntries().stream().map(this::map).toList())
                .build();
    }

    private TimesheetEntryDto map(TimesheetEntry e) {
        return TimesheetEntryDto.builder()
                .id(e.getId())
                .projectId(e.getProject().getId())
                .workDate(e.getWorkDate())
                .hours(e.getHours())
                .description(e.getDescription())
                .build();
    }
}

