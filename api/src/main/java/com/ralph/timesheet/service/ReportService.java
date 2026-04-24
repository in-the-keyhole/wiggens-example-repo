package com.ralph.timesheet.service;

import com.ralph.timesheet.dto.SummaryReportDTO;
import com.ralph.timesheet.model.Timesheet;
import com.ralph.timesheet.model.TimesheetEntry;
import com.ralph.timesheet.repository.TimesheetRepository;
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
public class ReportService {
    private final TimesheetRepository timesheetRepository;

    @Transactional(readOnly = true)
    public List<SummaryReportDTO> summary(LocalDate from, LocalDate to) {
        List<Timesheet> all = timesheetRepository.findAll();
        Map<Long, List<TimesheetEntry>> byEmp = all.stream()
                .flatMap(ts -> ts.getEntries().stream())
                .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
                .collect(Collectors.groupingBy(e -> e.getTimesheet().getEmployee().getId()));

        return byEmp.entrySet().stream().map(entry -> {
            Long empId = entry.getKey();
            List<TimesheetEntry> items = entry.getValue();
            String name = items.isEmpty() ? "" : items.get(0).getTimesheet().getEmployee().getFullName();
            BigDecimal total = items.stream()
                    .map(TimesheetEntry::getHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return SummaryReportDTO.builder()
                    .employeeId(empId)
                    .employeeName(name)
                    .fromDate(from)
                    .toDate(to)
                    .totalHours(total)
                    .build();
        }).collect(Collectors.toList());
    }
}

