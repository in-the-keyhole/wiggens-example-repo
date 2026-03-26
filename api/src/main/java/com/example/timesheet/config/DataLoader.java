package com.example.timesheet.config;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    private final EmployeeRepository employeeRepository;

    @Bean
    CommandLineRunner seedEmployees() {
        return args -> {
            if (employeeRepository.count() == 0) {
                employeeRepository.save(Employee.builder().name("Alice Adams").email("alice@example.com").build());
                employeeRepository.save(Employee.builder().name("Bob Brown").email("bob@example.com").build());
                employeeRepository.save(Employee.builder().name("Carol Clark").email("carol@example.com").build());
            }
        };
    }
}

