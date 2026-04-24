package com.ralph.timesheet.repository;

import com.ralph.timesheet.model.TimesheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, Long> {
}

