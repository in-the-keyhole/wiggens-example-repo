package com.wiggens.timesheet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmployeeDTO {
    Long id;
    @NotBlank(message = "name is required")
    String name;
    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    String email;
}

