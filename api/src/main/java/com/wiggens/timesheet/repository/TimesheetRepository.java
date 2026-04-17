package com.wiggens.timesheet.repository;

import com.wiggens.timesheet.domain.Employee;
import com.wiggens.timesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployee(Employee employee);
    List<Timesheet> findByWeekStartBetween(LocalDate start, LocalDate end);
}

