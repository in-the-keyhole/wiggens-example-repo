package com.example.timesheet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectDto {
    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;
}

