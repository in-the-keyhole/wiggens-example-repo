package com.example.timesheet.web;

import com.example.timesheet.service.EmployeeService;
import com.example.timesheet.web.dto.EmployeeCreateRequest;
import com.example.timesheet.web.dto.EmployeeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codex-example/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Validated @RequestBody EmployeeCreateRequest request) {
        com.example.timesheet.domain.Employee e = employeeService.create(request.getFirstName(), request.getLastName());
        return ResponseEntity.ok(EmployeeResponse.builder()
                .id(e.getId()).firstName(e.getFirstName()).lastName(e.getLastName()).build());
    }

    @GetMapping
    public List<EmployeeResponse> list() {
        return employeeService.list().stream()
                .map(e -> EmployeeResponse.builder()
                        .id(e.getId()).firstName(e.getFirstName()).lastName(e.getLastName()).build())
                .collect(Collectors.toList());
    }
}
