package com.wiggens.timesheet.repository;

import com.wiggens.timesheet.domain.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    Optional<Timesheet> findByEmployeeIdAndWeekStart(Long employeeId, LocalDate weekStart);
    List<Timesheet> findByEmployeeId(Long employeeId);
}

