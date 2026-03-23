package com.codexexample.timesheet.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectRequest {
    @NotBlank(message = "Project code is required")
    String code;

    @NotBlank(message = "Project name is required")
    String name;

    String description;
}
