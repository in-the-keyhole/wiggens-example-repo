package com.ralph.timesheet.service;

import com.ralph.timesheet.domain.Employee;
import com.ralph.timesheet.repository.EmployeeRepository;
import com.ralph.timesheet.web.dto.EmployeeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee emp = Employee.builder().name(dto.getName()).email(dto.getEmail()).build();
        emp = employeeRepository.save(emp);
        dto.setId(emp.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> list() {
        return employeeRepository.findAll().stream()
                .map(e -> EmployeeDTO.builder().id(e.getId()).name(e.getName()).email(e.getEmail()).build())
                .toList();
    }
}

