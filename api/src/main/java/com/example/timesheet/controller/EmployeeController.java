package com.example.timesheet.controller;

import com.example.timesheet.domain.Employee;
import com.example.timesheet.dto.EmployeeDto;
import com.example.timesheet.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codex-example/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto create(@Valid @RequestBody EmployeeDto dto) {
        return employeeService.create(dto);
    }

    @GetMapping
    public List<EmployeeDto> all() {
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeDto get(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {
        return employeeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
