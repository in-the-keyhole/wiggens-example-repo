package com.codexexample.timesheet.service;

import com.codexexample.timesheet.api.v1.dto.EmployeeHoursReportResponse;
import com.codexexample.timesheet.api.v1.dto.TimesheetEntryRequest;
import com.codexexample.timesheet.api.v1.dto.TimesheetEntryResponse;
import com.codexexample.timesheet.api.v1.dto.TimesheetRequest;
import com.codexexample.timesheet.api.v1.dto.TimesheetResponse;
import com.codexexample.timesheet.domain.Employee;
import com.codexexample.timesheet.domain.Project;
import com.codexexample.timesheet.domain.Timesheet;
import com.codexexample.timesheet.domain.TimesheetEntry;
import com.codexexample.timesheet.domain.TimesheetStatus;
import com.codexexample.timesheet.exception.BadRequestException;
import com.codexexample.timesheet.exception.ResourceNotFoundException;
import com.codexexample.timesheet.repository.EmployeeRepository;
import com.codexexample.timesheet.repository.ProjectRepository;
import com.codexexample.timesheet.repository.TimesheetEntryRepository;
import com.codexexample.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TimesheetEntryRepository timesheetEntryRepository;

    @Transactional
    public TimesheetResponse createTimesheet(TimesheetRequest request) {
        Employee employee = findEmployee(request.getEmployeeId());
        validateWeekStart(request.getWeekStart());
        timesheetRepository.findByEmployeeAndWeekStart(employee, request.getWeekStart())
                .ifPresent(existing -> {
                    throw new BadRequestException("Timesheet for this employee and week already exists");
                });
        Timesheet timesheet = Timesheet.builder()
                .employee(employee)
                .weekStart(request.getWeekStart())
                .status(TimesheetStatus.DRAFT)
                .build();
        List<TimesheetEntry> entries = request.getEntries().stream()
                .map(entryRequest -> toEntry(entryRequest, request.getWeekStart()))
                .toList();
        timesheet.replaceEntries(entries);
        Timesheet saved = timesheetRepository.save(timesheet);
        return toResponse(saved);
    }

    public TimesheetResponse getTimesheet(UUID id) {
        Timesheet timesheet = findTimesheet(id);
        return toResponse(timesheet);
    }

    public List<TimesheetResponse> getTimesheetsForEmployee(UUID employeeId) {
        return timesheetRepository.findByEmployeeId(employeeId).stream()
                .sorted(Comparator.comparing(Timesheet::getWeekStart).reversed())
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TimesheetResponse updateTimesheet(UUID id, TimesheetRequest request) {
        Timesheet timesheet = findTimesheet(id);
        if (!timesheet.getEmployee().getId().equals(request.getEmployeeId())) {
            throw new BadRequestException("Timesheet employee cannot be changed");
        }
        validateWeekStart(request.getWeekStart());
        if (!timesheet.getWeekStart().equals(request.getWeekStart())) {
            timesheetRepository.findByEmployeeAndWeekStart(timesheet.getEmployee(), request.getWeekStart())
                    .ifPresent(existing -> {
                        throw new BadRequestException("Timesheet for this employee and week already exists");
                    });
            timesheet.setWeekStart(request.getWeekStart());
        }
        List<TimesheetEntry> updatedEntries = request.getEntries().stream()
                .map(entryRequest -> toEntry(entryRequest, request.getWeekStart()))
                .toList();
        timesheet.replaceEntries(updatedEntries);
        return toResponse(timesheet);
    }

    public EmployeeHoursReportResponse getEmployeeHoursReport(UUID employeeId, LocalDate start, LocalDate end) {
        Employee employee = findEmployee(employeeId);
        LocalDate resolvedStart = start != null ? start : LocalDate.now().minusWeeks(4).with(DayOfWeek.MONDAY);
        LocalDate resolvedEnd = end != null ? end : resolvedStart.plusWeeks(4).minusDays(1);
        if (resolvedEnd.isBefore(resolvedStart)) {
            throw new BadRequestException("End date must be on or after start date");
        }
        List<TimesheetEntry> entries = timesheetEntryRepository
                .findByTimesheetEmployeeIdAndWorkDateBetween(employeeId, resolvedStart, resolvedEnd);
        BigDecimal totalHours = entries.stream()
                .map(TimesheetEntry::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        Map<Project, BigDecimal> hoursByProject = entries.stream()
                .collect(Collectors.groupingBy(TimesheetEntry::getProject,
                        Collectors.reducing(BigDecimal.ZERO, TimesheetEntry::getHours, BigDecimal::add)));
        List<EmployeeHoursReportResponse.ProjectHours> projectHours = hoursByProject.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Project::getCode)))
                .map(entry -> EmployeeHoursReportResponse.ProjectHours.builder()
                        .projectId(entry.getKey().getId())
                        .projectCode(entry.getKey().getCode())
                        .projectName(entry.getKey().getName())
                        .hours(entry.getValue().setScale(2, RoundingMode.HALF_UP))
                        .build())
                .toList();
        return EmployeeHoursReportResponse.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .startDate(resolvedStart)
                .endDate(resolvedEnd)
                .totalHours(totalHours)
                .projectHours(projectHours)
                .build();
    }

    private TimesheetResponse toResponse(Timesheet timesheet) {
        List<TimesheetEntryResponse> entryResponses = timesheet.getEntries().stream()
                .sorted(Comparator.comparing(TimesheetEntry::getWorkDate))
                .map(entry -> TimesheetEntryResponse.builder()
                        .id(entry.getId())
                        .projectId(entry.getProject().getId())
                        .projectCode(entry.getProject().getCode())
                        .projectName(entry.getProject().getName())
                        .workDate(entry.getWorkDate())
                        .hours(entry.getHours())
                        .notes(entry.getNotes())
                        .build())
                .toList();
        Employee employee = timesheet.getEmployee();
        return TimesheetResponse.builder()
                .id(timesheet.getId())
                .employeeId(employee.getId())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .weekStart(timesheet.getWeekStart())
                .status(timesheet.getStatus().name())
                .entries(entryResponses)
                .build();
    }

    private TimesheetEntry toEntry(TimesheetEntryRequest entryRequest, LocalDate weekStart) {
        if (entryRequest.getWorkDate().isBefore(weekStart) || entryRequest.getWorkDate().isAfter(weekStart.plusDays(6))) {
            throw new BadRequestException("Entry work date must be within the timesheet week");
        }
        Project project = projectRepository.findById(entryRequest.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return TimesheetEntry.builder()
                .project(project)
                .workDate(entryRequest.getWorkDate())
                .hours(entryRequest.getHours().setScale(2, RoundingMode.HALF_UP))
                .notes(entryRequest.getNotes())
                .build();
    }

    private void validateWeekStart(LocalDate weekStart) {
        if (weekStart.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new BadRequestException("Week start must be a Monday");
        }
    }

    private Timesheet findTimesheet(UUID id) {
        return timesheetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timesheet not found"));
    }

    private Employee findEmployee(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }
}
