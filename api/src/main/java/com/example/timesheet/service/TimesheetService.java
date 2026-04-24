package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Timesheet;
import com.example.timesheet.domain.TimesheetEntry;
import com.example.timesheet.dto.StatsDto;
import com.example.timesheet.dto.TimesheetDto;
import com.example.timesheet.dto.TimesheetEntryDto;
import com.example.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final EmployeeService employeeService;

    public TimesheetDto createOrUpdate(TimesheetDto dto) {
        Employee employee = employeeService.findEntityById(dto.getEmployeeId());
        LocalDate weekStart = normalizeToMonday(dto.getWeekStart());
        Timesheet ts = timesheetRepository.findByEmployeeAndWeekStart(employee, weekStart)
                .orElseGet(() -> Timesheet.builder().employee(employee).weekStart(weekStart).build());
        ts.getEntries().clear();
        if (dto.getEntries() != null) {
            for (TimesheetEntryDto e : dto.getEntries()) {
                TimesheetEntry entry = TimesheetEntry.builder()
                        .timesheet(ts)
                        .date(e.getDate())
                        .hours(e.getHours())
                        .build();
                ts.getEntries().add(entry);
            }
        }
        Timesheet saved = timesheetRepository.save(ts);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<TimesheetDto> findByEmployee(Long employeeId) {
        return timesheetRepository.findByEmployeeId(employeeId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TimesheetDto get(Long id) {
        return timesheetRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Timesheet not found: " + id));
    }

    public void delete(Long id) {
        timesheetRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StatsDto stats() {
        long employeeCount = employeeService.findAll().size();
        BigDecimal totalAll = timesheetRepository.findAll().stream()
                .flatMap(t -> t.getEntries().stream())
                .map(TimesheetEntry::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate thisMonday = normalizeToMonday(LocalDate.now());
        BigDecimal totalThisWeek = timesheetRepository.findAll().stream()
                .filter(t -> t.getWeekStart().equals(thisMonday))
                .flatMap(t -> t.getEntries().stream())
                .map(TimesheetEntry::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return StatsDto.builder()
                .employeeCount(employeeCount)
                .totalHoursAllTime(totalAll)
                .totalHoursThisWeek(totalThisWeek)
                .build();
    }

    private TimesheetDto toDto(Timesheet t) {
        return TimesheetDto.builder()
                .id(t.getId())
                .employeeId(t.getEmployee().getId())
                .weekStart(t.getWeekStart())
                .entries(t.getEntries().stream().map(e -> TimesheetEntryDto.builder()
                        .id(e.getId())
                        .date(e.getDate())
                        .hours(e.getHours())
                        .build()).collect(Collectors.toList()))
                .build();
    }

    private LocalDate normalizeToMonday(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.MONDAY) return date;
        int diff = date.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(diff);
    }
}
