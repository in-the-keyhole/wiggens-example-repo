package com.codexexample.timesheet.repository;

import com.codexexample.timesheet.domain.TimesheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, UUID> {

    List<TimesheetEntry> findByTimesheetEmployeeIdAndWorkDateBetween(UUID employeeId, LocalDate start, LocalDate end);
}
