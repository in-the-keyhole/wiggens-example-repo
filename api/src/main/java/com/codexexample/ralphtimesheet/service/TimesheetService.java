package com.codexexample.ralphtimesheet.service;

import com.codexexample.ralphtimesheet.domain.Employee;
import com.codexexample.ralphtimesheet.domain.Timesheet;
import com.codexexample.ralphtimesheet.dto.SummaryRowDto;
import com.codexexample.ralphtimesheet.dto.TimesheetDto;
import com.codexexample.ralphtimesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final EmployeeService employeeService;

    @Transactional
    public TimesheetDto upsert(Long employeeId, LocalDate weekStart, double mon, double tue, double wed, double thu, double fri, double sat, double sun, String notes) {
        if (!isMonday(weekStart)) throw new IllegalArgumentException("weekStart must be a Monday");
        validateHours(mon); validateHours(tue); validateHours(wed); validateHours(thu); validateHours(fri); validateHours(sat); validateHours(sun);

        Employee employee = employeeService.getEntity(employeeId);
        Timesheet ts = timesheetRepository.findByEmployeeIdAndWeekStart(employeeId, weekStart)
                .orElse(Timesheet.builder().employee(employee).weekStart(weekStart).build());
        ts.setMon(mon); ts.setTue(tue); ts.setWed(wed); ts.setThu(thu); ts.setFri(fri); ts.setSat(sat); ts.setSun(sun); ts.setNotes(notes);
        Timesheet saved = timesheetRepository.save(ts);
        return toDto(saved);
    }

    public TimesheetDto get(Long employeeId, LocalDate weekStart) {
        Timesheet ts = timesheetRepository.findByEmployeeIdAndWeekStart(employeeId, weekStart)
                .orElseThrow(() -> new IllegalArgumentException("Timesheet not found"));
        return toDto(ts);
    }

    public List<SummaryRowDto> summary(LocalDate from, LocalDate to) {
        return timesheetRepository.findByWeekStartBetween(from, to).stream()
                .collect(java.util.stream.Collectors.groupingBy(Timesheet::getEmployee,
                        java.util.stream.Collectors.summingDouble(t -> t.getMon()+t.getTue()+t.getWed()+t.getThu()+t.getFri()+t.getSat()+t.getSun())))
                .entrySet().stream()
                .map(e -> SummaryRowDto.builder().employeeId(e.getKey().getId()).employeeName(e.getKey().getName()).totalHours(e.getValue()).build())
                .sorted(Comparator.comparing(SummaryRowDto::getEmployeeName))
                .toList();
    }

    private TimesheetDto toDto(Timesheet t) {
        return TimesheetDto.builder()
                .id(t.getId())
                .employeeId(t.getEmployee().getId())
                .weekStart(t.getWeekStart())
                .mon(t.getMon()).tue(t.getTue()).wed(t.getWed()).thu(t.getThu()).fri(t.getFri()).sat(t.getSat()).sun(t.getSun())
                .notes(t.getNotes())
                .build();
    }

    private void validateHours(double h) {
        if (h < 0.0 || h > 24.0) throw new IllegalArgumentException("hours must be between 0 and 24");
    }
    private boolean isMonday(LocalDate d) { return d != null && d.getDayOfWeek().getValue() == 1; }
}

