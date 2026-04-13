package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Timesheet;
import com.example.timesheet.domain.TimesheetEntry;
import com.example.timesheet.dto.SummaryReportDTO;
import com.example.timesheet.dto.TimesheetItemDTO;
import com.example.timesheet.dto.TimesheetRequestDTO;
import com.example.timesheet.dto.TimesheetResponseDTO;
import com.example.timesheet.repository.EmployeeRepository;
import com.example.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    @Transactional
    public TimesheetResponseDTO upsertTimesheet(TimesheetRequestDTO request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseGet(() -> employeeRepository.save(Employee.builder().name("Employee " + request.getEmployeeId()).id(request.getEmployeeId()).build()));

        LocalDate weekStart = request.getWeekStart();
        Timesheet timesheet = timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElseGet(() -> Timesheet.builder().employee(employee).weekStart(weekStart).build());

        timesheet.getEntries().clear();
        for (TimesheetItemDTO item : request.getEntries()) {
            TimesheetEntry entry = TimesheetEntry.builder()
                    .timesheet(timesheet)
                    .workDate(item.getWorkDate())
                    .project(item.getProject())
                    .hours(item.getHours())
                    .note(item.getNote())
                    .build();
            timesheet.getEntries().add(entry);
        }

        Timesheet saved = timesheetRepository.save(timesheet);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public TimesheetResponseDTO getTimesheet(Long employeeId, LocalDate weekStart) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));
        Timesheet ts = timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found for weekStart: " + weekStart));
        return toDto(ts);
    }

    @Transactional(readOnly = true)
    public SummaryReportDTO getSummary(LocalDate from, LocalDate to) {
        List<Timesheet> all = timesheetRepository.findAll();
        Map<String, Double> totals = all.stream()
                .flatMap(ts -> ts.getEntries().stream())
                .filter(e -> !e.getWorkDate().isBefore(from) && !e.getWorkDate().isAfter(to))
                .collect(Collectors.groupingBy(e -> e.getTimesheet().getEmployee().getName(), Collectors.summingDouble(TimesheetEntry::getHours)));
        return SummaryReportDTO.builder().totalsByEmployee(totals).build();
    }

    private TimesheetResponseDTO toDto(Timesheet saved) {
        List<TimesheetItemDTO> items = saved.getEntries().stream()
                .sorted(Comparator.comparing(TimesheetEntry::getWorkDate))
                .map(e -> TimesheetItemDTO.builder()
                        .workDate(e.getWorkDate())
                        .project(e.getProject())
                        .hours(e.getHours())
                        .note(e.getNote())
                        .build())
                .toList();
        return TimesheetResponseDTO.builder()
                .id(saved.getId())
                .employeeId(saved.getEmployee().getId())
                .employeeName(saved.getEmployee().getName())
                .weekStart(saved.getWeekStart())
                .entries(items)
                .build();
    }
}

