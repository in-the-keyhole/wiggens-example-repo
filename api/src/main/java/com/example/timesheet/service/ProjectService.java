package com.example.timesheet.service;

import com.example.timesheet.domain.Project;
import com.example.timesheet.repo.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project create(String name) {
        return projectRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> projectRepository.save(Project.builder().name(name).build()));
    }

    public List<Project> list() {
        return projectRepository.findAll();
    }

    public Project get(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
    }
}

