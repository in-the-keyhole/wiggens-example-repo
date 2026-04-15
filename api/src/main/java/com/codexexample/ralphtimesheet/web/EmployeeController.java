package com.codexexample.ralphtimesheet.web;

import com.codexexample.ralphtimesheet.dto.CreateEmployeeRequest;
import com.codexexample.ralphtimesheet.dto.EmployeeDto;
import com.codexexample.ralphtimesheet.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codex-example/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto create(@Valid @RequestBody CreateEmployeeRequest req) {
        return employeeService.create(req.getName(), req.getEmail());
    }

    @GetMapping
    public List<EmployeeDto> list() {
        return employeeService.list();
    }
}

