package com.example.timesheet.api;

import com.example.timesheet.api.dto.TimesheetDTO;
import com.example.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/codex-example/api/v1/timesheets")
public class TimesheetController {
    private final TimesheetService service;

    public TimesheetController(TimesheetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TimesheetDTO> create(@Valid @RequestBody TimesheetDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimesheetDTO> get(@PathVariable Long id) {
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimesheetDTO> update(@PathVariable Long id, @Valid @RequestBody TimesheetDTO dto) {
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TimesheetDTO>> findByEmployeeAndWeek(@RequestParam String employeeId,
                                                                    @RequestParam String weekStart) {
        LocalDate ws = LocalDate.parse(weekStart);
        return ResponseEntity.ok(service.findByEmployeeAndWeek(employeeId, ws));
    }
}

