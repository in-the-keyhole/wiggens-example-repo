package com.wiggens.timesheet.service;

import com.wiggens.timesheet.domain.Employee;
import com.wiggens.timesheet.dto.EmployeeDTO;
import com.wiggens.timesheet.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee emp = Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
        emp = employeeRepository.save(emp);
        return toDTO(emp);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAll() {
        return employeeRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findById(id).map(this::toDTO);
    }

    @Transactional
    public Optional<EmployeeDTO> update(Long id, EmployeeDTO dto) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setName(dto.getName());
            emp.setEmail(dto.getEmail());
            return toDTO(employeeRepository.save(emp));
        });
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO toDTO(Employee e) {
        return EmployeeDTO.builder().id(e.getId()).name(e.getName()).email(e.getEmail()).build();
    }
}

