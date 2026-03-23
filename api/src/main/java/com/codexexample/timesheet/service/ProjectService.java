package com.codexexample.timesheet.service;

import com.codexexample.timesheet.api.v1.dto.ProjectRequest;
import com.codexexample.timesheet.api.v1.dto.ProjectResponse;
import com.codexexample.timesheet.domain.Project;
import com.codexexample.timesheet.exception.BadRequestException;
import com.codexexample.timesheet.exception.ResourceNotFoundException;
import com.codexexample.timesheet.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        if (projectRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new BadRequestException("A project with this code already exists");
        }
        Project project = Project.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> getProjects() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse getProject(UUID id) {
        return toResponse(findById(id));
    }

    @Transactional
    public ProjectResponse updateProject(UUID id, ProjectRequest request) {
        Project project = findById(id);
        if (!project.getCode().equalsIgnoreCase(request.getCode())
                && projectRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new BadRequestException("A project with this code already exists");
        }
        project.setCode(request.getCode());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return toResponse(project);
    }

    @Transactional
    public void deleteProject(UUID id) {
        Project project = findById(id);
        projectRepository.delete(project);
    }

    public Project findById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .code(project.getCode())
                .name(project.getName())
                .description(project.getDescription())
                .build();
    }
}
