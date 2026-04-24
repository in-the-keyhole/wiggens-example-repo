package com.example.timesheet.repository;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployeeId(Long employeeId);
    Optional<Timesheet> findByEmployeeAndWeekStart(Employee employee, LocalDate weekStart);
}

