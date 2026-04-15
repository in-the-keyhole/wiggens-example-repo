package com.ralph.timesheet.repository;

import com.ralph.timesheet.domain.Employee;
import com.ralph.timesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    Optional<Timesheet> findByEmployeeAndWeekStart(Employee employee, LocalDate weekStart);
    List<Timesheet> findByWeekStartBetween(LocalDate start, LocalDate end);
}

