package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee create(String firstName, String lastName) {
        Employee e = Employee.builder().firstName(firstName).lastName(lastName).build();
        return employeeRepository.save(e);
    }

    public List<Employee> list() {
        return employeeRepository.findAll();
    }

    public Employee get(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }
}

