package com.codexexample.ralphtimesheet.service;

import com.codexexample.ralphtimesheet.domain.Employee;
import com.codexexample.ralphtimesheet.dto.EmployeeDto;
import com.codexexample.ralphtimesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeDto create(String name, String email) {
        Employee employee = employeeRepository.save(Employee.builder().name(name).email(email).build());
        return toDto(employee);
    }

    public List<EmployeeDto> list() {
        return employeeRepository.findAll().stream().map(this::toDto).toList();
    }

    public Employee getEntity(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    private EmployeeDto toDto(Employee e) {
        return EmployeeDto.builder().id(e.getId()).name(e.getName()).email(e.getEmail()).build();
    }
}

