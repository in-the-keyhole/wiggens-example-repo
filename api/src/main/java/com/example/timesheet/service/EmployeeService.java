package com.example.timesheet.service;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.dto.EmployeeDto;
import com.example.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();
        Employee saved = employeeRepository.save(employee);
        dto.setId(saved.getId());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeDto findById(Long id) {
        return employeeRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + id));
    }

    @Transactional(readOnly = true)
    public Employee findEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + id));
    }

    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee e = findEntityById(id);
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setEmail(dto.getEmail());
        return toDto(e);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeDto toDto(Employee e) {
        return EmployeeDto.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .build();
    }
}
