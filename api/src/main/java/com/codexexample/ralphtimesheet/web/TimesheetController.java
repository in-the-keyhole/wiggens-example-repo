package com.codexexample.ralphtimesheet.web;

import com.codexexample.ralphtimesheet.dto.SummaryRowDto;
import com.codexexample.ralphtimesheet.dto.TimesheetDto;
import com.codexexample.ralphtimesheet.dto.UpsertTimesheetRequest;
import com.codexexample.ralphtimesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codex-example/api/v1")
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping("/timesheets")
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetDto upsert(@Valid @RequestBody UpsertTimesheetRequest req) {
        return timesheetService.upsert(
                req.getEmployeeId(), req.getWeekStart(),
                req.getMon(), req.getTue(), req.getWed(), req.getThu(), req.getFri(), req.getSat(), req.getSun(),
                req.getNotes()
        );
    }

    @GetMapping("/timesheets")
    public TimesheetDto get(@RequestParam Long employeeId,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        return timesheetService.get(employeeId, weekStart);
    }

    @GetMapping("/reports/summary")
    public List<SummaryRowDto> summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return timesheetService.summary(from, to);
    }
}

