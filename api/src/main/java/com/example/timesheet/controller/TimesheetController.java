package com.example.timesheet.controller;

import com.example.timesheet.dto.StatsDto;
import com.example.timesheet.dto.TimesheetDto;
import com.example.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codex-example/api/v1/timesheets")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetDto createOrUpdate(@Valid @RequestBody TimesheetDto dto) {
        return timesheetService.createOrUpdate(dto);
    }

    @GetMapping("/employee/{employeeId}")
    public List<TimesheetDto> byEmployee(@PathVariable Long employeeId) {
        return timesheetService.findByEmployee(employeeId);
    }

    @GetMapping("/{id}")
    public TimesheetDto get(@PathVariable Long id) {
        return timesheetService.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        timesheetService.delete(id);
    }

    @GetMapping("/stats")
    public StatsDto stats() {
        return timesheetService.stats();
    }
}

