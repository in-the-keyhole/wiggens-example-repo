package com.ralph.timesheet.service;

import com.ralph.timesheet.domain.Employee;
import com.ralph.timesheet.domain.Timesheet;
import com.ralph.timesheet.repository.EmployeeRepository;
import com.ralph.timesheet.repository.TimesheetRepository;
import com.ralph.timesheet.web.dto.SummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final EmployeeRepository employeeRepository;
    private final TimesheetRepository timesheetRepository;

    @Transactional(readOnly = true)
    public List<SummaryDTO> summary(LocalDate from, LocalDate to) {
        return employeeRepository.findAll().stream().map(emp -> {
            List<Timesheet> sheets = timesheetRepository.findByEmployeeAndWeekStartBetween(emp, from, to);
            BigDecimal total = sheets.stream()
                    .map(ts -> ts.getMon().add(ts.getTue()).add(ts.getWed()).add(ts.getThu())
                            .add(ts.getFri()).add(ts.getSat()).add(ts.getSun()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return SummaryDTO.builder().employeeId(emp.getId()).employeeName(emp.getName()).totalHours(total).build();
        }).toList();
    }
}

