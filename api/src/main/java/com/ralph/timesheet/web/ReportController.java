package com.ralph.timesheet.web;

import com.ralph.timesheet.service.ReportService;
import com.ralph.timesheet.web.dto.SummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codex-example/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/summary")
    public List<SummaryDTO> summary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.summary(from, to);
    }
}

