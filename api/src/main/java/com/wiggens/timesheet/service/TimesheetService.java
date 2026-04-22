package com.wiggens.timesheet.service;

import com.wiggens.timesheet.domain.Employee;
import com.wiggens.timesheet.domain.Timesheet;
import com.wiggens.timesheet.dto.TimesheetDTO;
import com.wiggens.timesheet.repository.EmployeeRepository;
import com.wiggens.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public TimesheetDTO createOrUpdate(TimesheetDTO dto) {
        Employee emp = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + dto.getEmployeeId()));
        LocalDate week = dto.getWeekStart();
        Timesheet ts = timesheetRepository.findByEmployeeIdAndWeekStart(emp.getId(), week)
                .orElseGet(() -> Timesheet.builder().employee(emp).weekStart(week).build());
        apply(dto, ts);
        ts = timesheetRepository.save(ts);
        return toDTO(ts);
    }

    @Transactional(readOnly = true)
    public Optional<TimesheetDTO> findByEmployeeAndWeek(Long employeeId, LocalDate weekStart) {
        return timesheetRepository.findByEmployeeIdAndWeekStart(employeeId, weekStart).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<TimesheetDTO> findByEmployee(Long employeeId) {
        return timesheetRepository.findByEmployeeId(employeeId).stream().map(this::toDTO).toList();
    }

    private void apply(TimesheetDTO dto, Timesheet ts) {
        ts.setHoursMon(dto.getHoursMon());
        ts.setHoursTue(dto.getHoursTue());
        ts.setHoursWed(dto.getHoursWed());
        ts.setHoursThu(dto.getHoursThu());
        ts.setHoursFri(dto.getHoursFri());
        ts.setHoursSat(dto.getHoursSat());
        ts.setHoursSun(dto.getHoursSun());
    }

    private TimesheetDTO toDTO(Timesheet ts) {
        return TimesheetDTO.builder()
                .id(ts.getId())
                .employeeId(ts.getEmployee().getId())
                .weekStart(ts.getWeekStart())
                .hoursMon(ts.getHoursMon())
                .hoursTue(ts.getHoursTue())
                .hoursWed(ts.getHoursWed())
                .hoursThu(ts.getHoursThu())
                .hoursFri(ts.getHoursFri())
                .hoursSat(ts.getHoursSat())
                .hoursSun(ts.getHoursSun())
                .build();
    }
}

