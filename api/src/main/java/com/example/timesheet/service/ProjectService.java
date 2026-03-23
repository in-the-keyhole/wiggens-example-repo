package com.example.timesheet.service;

import com.example.timesheet.domain.Project;
import com.example.timesheet.dto.ProjectDto;
import com.example.timesheet.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectDto create(ProjectDto dto) {
        if (projectRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Project code already exists");
        }
        Project saved = projectRepository.save(Project.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .build());
        return ProjectDto.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name(saved.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public Project getEntity(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }
}

