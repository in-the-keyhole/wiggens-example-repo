package com.codexexample.ralphtimesheet.repository;

import com.codexexample.ralphtimesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    Optional<Timesheet> findByEmployeeIdAndWeekStart(Long employeeId, LocalDate weekStart);
    List<Timesheet> findByWeekStartBetween(LocalDate from, LocalDate to);
}

