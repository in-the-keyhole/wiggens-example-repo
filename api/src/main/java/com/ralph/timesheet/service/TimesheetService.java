package com.ralph.timesheet.service;

import com.ralph.timesheet.dto.TimesheetEntryDTO;
import com.ralph.timesheet.dto.TimesheetRequestDTO;
import com.ralph.timesheet.dto.TimesheetResponseDTO;
import com.ralph.timesheet.model.Employee;
import com.ralph.timesheet.model.Timesheet;
import com.ralph.timesheet.model.TimesheetEntry;
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
    private final TimesheetRepository timesheetRepository;
    private final EmployeeService employeeService;

    @Transactional
    public TimesheetResponseDTO upsertTimesheet(TimesheetRequestDTO req) {
        Employee employee = employeeService.getById(req.getEmployeeId());
        Timesheet timesheet = timesheetRepository
                .findByEmployeeAndWeekStart(employee, req.getWeekStart())
                .orElseGet(() -> Timesheet.builder()
                        .employee(employee)
                        .weekStart(req.getWeekStart())
                        .build());

        List<TimesheetEntry> entries = req.getEntries().stream().map(e -> TimesheetEntry.builder()
                .date(e.getDate())
                .project(e.getProject())
                .hours(e.getHours())
                .build()).collect(Collectors.toList());
        timesheet.setEntries(entries);

        Timesheet saved = timesheetRepository.save(timesheet);
        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public TimesheetResponseDTO getTimesheet(Long employeeId, LocalDate weekStart) {
        Employee employee = employeeService.getById(employeeId);
        Timesheet timesheet = timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));
        return toResponseDTO(timesheet);
    }

    private TimesheetResponseDTO toResponseDTO(Timesheet ts) {
        List<TimesheetEntryDTO> entryDTOs = ts.getEntries().stream().map(e ->
                TimesheetEntryDTO.builder()
                        .id(e.getId())
                        .date(e.getDate())
                        .project(e.getProject())
                        .hours(e.getHours())
                        .build()
        ).collect(Collectors.toList());
        BigDecimal total = entryDTOs.stream()
                .map(TimesheetEntryDTO::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return TimesheetResponseDTO.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .employeeName(ts.getEmployee().getFullName())
                .weekStart(ts.getWeekStart())
                .entries(entryDTOs)
                .totalHours(total)
                .build();
    }
}

