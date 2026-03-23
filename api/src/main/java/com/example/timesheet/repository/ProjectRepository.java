package com.example.timesheet.repository;

import com.example.timesheet.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByCode(String code);
}

