package com.codexexample.timesheet.repository;

import com.codexexample.timesheet.domain.Employee;
import com.codexexample.timesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimesheetRepository extends JpaRepository<Timesheet, UUID> {

    Optional<Timesheet> findByEmployeeAndWeekStart(Employee employee, LocalDate weekStart);

    List<Timesheet> findByEmployeeId(UUID employeeId);
}
