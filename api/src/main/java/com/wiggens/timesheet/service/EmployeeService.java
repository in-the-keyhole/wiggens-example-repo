package com.wiggens.timesheet.service;

import com.wiggens.timesheet.domain.Employee;
import com.wiggens.timesheet.dto.EmployeeDto;
import com.wiggens.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeDto> list() {
        return employeeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public EmployeeDto create(EmployeeDto dto) {
        Employee e = Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .build();
        e = employeeRepository.save(e);
        return toDto(e);
    }

    public EmployeeDto get(Long id) {
        return toDto(employeeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Employee not found")));
    }

    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee e = employeeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Employee not found"));
        e.setFirstName(dto.getFirstName());
        e.setLastName(dto.getLastName());
        e.setEmail(dto.getEmail());
        return toDto(employeeRepository.save(e));
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    public EmployeeDto toDto(Employee e) {
        return EmployeeDto.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .email(e.getEmail())
                .build();
    }
}

