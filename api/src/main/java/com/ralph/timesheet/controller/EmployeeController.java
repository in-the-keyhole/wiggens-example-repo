package com.ralph.timesheet.controller;

import com.ralph.timesheet.dto.EmployeeDTO;
import com.ralph.timesheet.service.EmployeeService;
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
    public EmployeeDTO create(@Valid @RequestBody EmployeeDTO dto) {
        return employeeService.create(dto);
    }

    @GetMapping
    public List<EmployeeDTO> list() {
        return employeeService.list();
    }
}

