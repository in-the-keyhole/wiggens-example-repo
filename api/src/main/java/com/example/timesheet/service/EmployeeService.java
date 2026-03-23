package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.dto.EmployeeDto;
import com.example.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        Employee saved = employeeRepository.save(Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build());
        return EmployeeDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public Employee getEntity(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }
}

