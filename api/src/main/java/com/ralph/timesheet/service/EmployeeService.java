package com.ralph.timesheet.service;

import com.ralph.timesheet.domain.Employee;
import com.ralph.timesheet.dto.EmployeeDto;
import com.ralph.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
        Employee saved = employeeRepository.save(employee);
        return EmployeeDto.builder().id(saved.getId()).name(saved.getName()).email(saved.getEmail()).build();
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> list() {
        return employeeRepository.findAll().stream()
                .map(e -> EmployeeDto.builder().id(e.getId()).name(e.getName()).email(e.getEmail()).build())
                .collect(Collectors.toList());
    }
}

