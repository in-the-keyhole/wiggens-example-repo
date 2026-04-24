package com.ralph.timesheet.service;

import com.ralph.timesheet.dto.EmployeeDTO;
import com.ralph.timesheet.model.Employee;
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
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee employee = Employee.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .build();
        Employee saved = employeeRepository.save(employee);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> list() {
        return employeeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    private EmployeeDTO toDTO(Employee e) {
        return EmployeeDTO.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .email(e.getEmail())
                .build();
    }
}

