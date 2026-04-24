package com.ralph.timesheet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDTO {
    private Long id;
    @NotBlank
    private String fullName;
    @NotBlank
    @Email
    private String email;
}

