package com.ralph.timesheet.web;

import com.ralph.timesheet.service.TimesheetService;
import com.ralph.timesheet.web.dto.TimesheetRequestDTO;
import com.ralph.timesheet.web.dto.TimesheetResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codex-example/api/v1/timesheets")
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetResponseDTO submit(@Valid @RequestBody TimesheetRequestDTO dto) {
        return timesheetService.createOrUpdate(dto);
    }

    @GetMapping
    public TimesheetResponseDTO get(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return timesheetService.get(employeeId, weekStart);
    }
}

