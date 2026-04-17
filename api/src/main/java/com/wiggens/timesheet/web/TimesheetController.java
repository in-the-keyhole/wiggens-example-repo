package com.wiggens.timesheet.web;

import com.wiggens.timesheet.dto.TimesheetDto;
import com.wiggens.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/codex-example/api/v1")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @GetMapping("/timesheets")
    public List<TimesheetDto> list() { return timesheetService.list(); }

    @PostMapping("/timesheets")
    public ResponseEntity<TimesheetDto> create(@Valid @RequestBody TimesheetDto dto) {
        TimesheetDto created = timesheetService.create(dto);
        return ResponseEntity.created(URI.create("/codex-example/api/v1/timesheets/" + created.getId())).body(created);
    }

    @GetMapping("/timesheets/{id}")
    public TimesheetDto get(@PathVariable Long id) { return timesheetService.get(id); }

    @PutMapping("/timesheets/{id}")
    public TimesheetDto update(@PathVariable Long id, @Valid @RequestBody TimesheetDto dto) { return timesheetService.update(id, dto); }

    @GetMapping("/reports/summary")
    public Map<String, Object> summary() {
        Map<String, Object> m = new HashMap<>();
        LocalDate weekStart = TimesheetService.weekStartFor(LocalDate.now());
        m.put("weekStart", weekStart);
        m.put("totalHoursThisWeek", timesheetService.totalHoursForWeek(weekStart));
        m.put("totalHoursAllTime", timesheetService.totalHoursAllTime());
        return m;
    }
}

