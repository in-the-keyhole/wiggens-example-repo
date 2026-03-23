package com.codexexample.timesheet.service;

import com.codexexample.timesheet.api.v1.dto.EmployeeRequest;
import com.codexexample.timesheet.api.v1.dto.EmployeeResponse;
import com.codexexample.timesheet.domain.Employee;
import com.codexexample.timesheet.exception.BadRequestException;
import com.codexexample.timesheet.exception.ResourceNotFoundException;
import com.codexexample.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BadRequestException("An employee with this email already exists");
        }
        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .active(request.getActive())
                .build();
        Employee saved = employeeRepository.save(employee);
        return toResponse(saved);
    }

    public List<EmployeeResponse> getEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EmployeeResponse getEmployee(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public EmployeeResponse updateEmployee(UUID id, EmployeeRequest request) {
        Employee employee = findById(id);
        employeeRepository.findByEmailIgnoreCase(request.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("An employee with this email already exists");
                });
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setActive(request.getActive());
        return toResponse(employee);
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
    }

    public Employee findById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    private EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .active(employee.getActive())
                .build();
    }
}
