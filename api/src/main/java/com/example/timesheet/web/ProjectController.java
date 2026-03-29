package com.example.timesheet.web;

import com.example.timesheet.service.ProjectService;
import com.example.timesheet.web.dto.ProjectCreateRequest;
import com.example.timesheet.web.dto.ProjectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/codex-example/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Validated @RequestBody ProjectCreateRequest request) {
        var p = projectService.create(request.getName());
        return ResponseEntity.ok(ProjectResponse.builder().id(p.getId()).name(p.getName()).build());
    }

    @GetMapping
    public List<ProjectResponse> list() {
        return projectService.list().stream()
                .map(p -> ProjectResponse.builder().id(p.getId()).name(p.getName()).build())
                .collect(Collectors.toList());
    }
}
