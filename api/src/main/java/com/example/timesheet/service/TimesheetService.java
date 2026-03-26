package com.example.timesheet.service;

import com.example.timesheet.api.dto.TimesheetDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TimesheetService {
    private final Map<Long, TimesheetDTO> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public TimesheetDTO create(TimesheetDTO dto) {
        long id = idSeq.getAndIncrement();
        dto.setId(id);
        store.put(id, dto);
        return dto;
    }

    public Optional<TimesheetDTO> get(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Optional<TimesheetDTO> update(Long id, TimesheetDTO dto) {
        if (!store.containsKey(id)) return Optional.empty();
        dto.setId(id);
        store.put(id, dto);
        return Optional.of(dto);
    }

    public List<TimesheetDTO> findByEmployeeAndWeek(String employeeId, LocalDate weekStart) {
        List<TimesheetDTO> res = new ArrayList<>();
        for (TimesheetDTO t : store.values()) {
            if (t.getEmployeeId().equals(employeeId) && t.getWeekStart().equals(weekStart)) {
                res.add(t);
            }
        }
        return res;
    }

    public int totalHoursForWeek(LocalDate weekStart) {
        return store.values().stream()
                .filter(t -> t.getWeekStart().equals(weekStart))
                .flatMap(t -> t.getEntries().stream())
                .mapToInt(e -> Optional.ofNullable(e.getHours()).orElse(0))
                .sum();
    }
}

