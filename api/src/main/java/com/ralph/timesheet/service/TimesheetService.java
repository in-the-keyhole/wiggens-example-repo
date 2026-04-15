package com.ralph.timesheet.service;

import com.ralph.timesheet.domain.Employee;
import com.ralph.timesheet.domain.Timesheet;
import com.ralph.timesheet.repository.EmployeeRepository;
import com.ralph.timesheet.repository.TimesheetRepository;
import com.ralph.timesheet.web.dto.TimesheetRequestDTO;
import com.ralph.timesheet.web.dto.TimesheetResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public TimesheetResponseDTO createOrUpdate(TimesheetRequestDTO dto) {
        Employee emp = employeeRepository.findById(dto.getEmployeeId()).orElseThrow();

        Timesheet ts = timesheetRepository.findByEmployeeIdAndWeekStart(emp.getId(), dto.getWeekStart())
                .orElse(Timesheet.builder().employee(emp).weekStart(dto.getWeekStart()).build());

        ts.setMon(dto.getMon());
        ts.setTue(dto.getTue());
        ts.setWed(dto.getWed());
        ts.setThu(dto.getThu());
        ts.setFri(dto.getFri());
        ts.setSat(dto.getSat());
        ts.setSun(dto.getSun());

        ts = timesheetRepository.save(ts);

        return toResponse(ts);
    }

    @Transactional(readOnly = true)
    public TimesheetResponseDTO get(Long employeeId, java.time.LocalDate weekStart) {
        Timesheet ts = timesheetRepository.findByEmployeeIdAndWeekStart(employeeId, weekStart).orElseThrow();
        return toResponse(ts);
    }

    private TimesheetResponseDTO toResponse(Timesheet ts) {
        BigDecimal total = ts.getMon().add(ts.getTue()).add(ts.getWed()).add(ts.getThu())
                .add(ts.getFri()).add(ts.getSat()).add(ts.getSun());
        return TimesheetResponseDTO.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .weekStart(ts.getWeekStart())
                .mon(ts.getMon())
                .tue(ts.getTue())
                .wed(ts.getWed())
                .thu(ts.getThu())
                .fri(ts.getFri())
                .sat(ts.getSat())
                .sun(ts.getSun())
                .total(total)
                .build();
    }
}

