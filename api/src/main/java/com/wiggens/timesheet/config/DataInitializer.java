package com.wiggens.timesheet.config;

import com.wiggens.timesheet.domain.Employee;
import com.wiggens.timesheet.domain.Timesheet;
import com.wiggens.timesheet.domain.TimesheetEntry;
import com.wiggens.timesheet.repository.EmployeeRepository;
import com.wiggens.timesheet.repository.TimesheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner seed(EmployeeRepository employees, TimesheetRepository timesheets) {
        return args -> {
            if (employees.count() > 0) return;
            Employee a = employees.save(Employee.builder().firstName("Ralph").lastName("Wiggins").email("ralph@example.com").build());
            Employee b = employees.save(Employee.builder().firstName("Lisa").lastName("Simpson").email("lisa@example.com").build());

            LocalDate week = LocalDate.now().with(java.time.DayOfWeek.MONDAY);

            Timesheet t1 = Timesheet.builder().employee(a).weekStart(week).build();
            t1.getEntries().addAll(List.of(
                    TimesheetEntry.builder().timesheet(t1).dayOfWeek(1).hours(8).project("ACME").notes("New feature").build(),
                    TimesheetEntry.builder().timesheet(t1).dayOfWeek(2).hours(8).project("ACME").build(),
                    TimesheetEntry.builder().timesheet(t1).dayOfWeek(3).hours(8).project("ACME").build()
            ));
            timesheets.save(t1);

            Timesheet t2 = Timesheet.builder().employee(b).weekStart(week).build();
            t2.getEntries().addAll(List.of(
                    TimesheetEntry.builder().timesheet(t2).dayOfWeek(1).hours(6).project("Springfield").build(),
                    TimesheetEntry.builder().timesheet(t2).dayOfWeek(2).hours(7.5).project("Springfield").build()
            ));
            timesheets.save(t2);
        };
    }
}

