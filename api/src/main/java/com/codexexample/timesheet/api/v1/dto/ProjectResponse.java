package com.codexexample.timesheet.api.v1.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ProjectResponse {
    UUID id;
    String code;
    String name;
    String description;
}
