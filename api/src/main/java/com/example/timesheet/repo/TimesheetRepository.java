package com.example.timesheet.repo;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    Optional<Timesheet> findByEmployeeAndWeekStart(Employee employee, LocalDate weekStart);
}

