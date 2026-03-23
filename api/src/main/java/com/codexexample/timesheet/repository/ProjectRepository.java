package com.codexexample.timesheet.repository;

import com.codexexample.timesheet.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    boolean existsByCodeIgnoreCase(String code);
}
