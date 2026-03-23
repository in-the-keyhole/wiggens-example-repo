package com.example.timesheet.web;

import com.example.timesheet.dto.TimesheetDto;
import com.example.timesheet.dto.TimesheetEntryDto;
import com.example.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("codex-example/api/v1/timesheets")
@RequiredArgsConstructor
public class TimesheetController {
    private final TimesheetService timesheetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetDto create(@Valid @RequestBody TimesheetDto dto) {
        return timesheetService.create(dto);
    }

    @GetMapping("/{id}")
    public TimesheetDto get(@PathVariable Long id) {
        return timesheetService.get(id);
    }

    @PostMapping("/{id}/entries")
    @ResponseStatus(HttpStatus.CREATED)
    public TimesheetEntryDto addEntry(@PathVariable Long id, @Valid @RequestBody TimesheetEntryDto dto) {
        return timesheetService.addEntry(id, dto);
    }

    @PutMapping("/{id}/entries/{entryId}")
    public TimesheetEntryDto updateEntry(@PathVariable Long id, @PathVariable Long entryId, @Valid @RequestBody TimesheetEntryDto dto) {
        return timesheetService.updateEntry(id, entryId, dto);
    }

    @DeleteMapping("/{id}/entries/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable Long id, @PathVariable Long entryId) {
        timesheetService.deleteEntry(id, entryId);
    }
}

