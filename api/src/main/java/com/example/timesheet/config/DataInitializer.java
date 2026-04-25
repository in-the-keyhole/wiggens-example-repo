package com.example.timesheet.config;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final EmployeeRepository employeeRepository;

    @Bean
    CommandLineRunner seedEmployees() {
        return args -> {
            employeeRepository.findByEmail("john.doe@example.com").orElseGet(() -> {
                Employee e = Employee.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .build();
                Employee saved = employeeRepository.save(e);
                log.info("Seeded example employee with id={} email={}", saved.getId(), saved.getEmail());
                return saved;
            });
        };
    }
}

