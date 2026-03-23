package com.example.timesheet.repository;

import com.example.timesheet.domain.TimesheetEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry, Long> {
}

