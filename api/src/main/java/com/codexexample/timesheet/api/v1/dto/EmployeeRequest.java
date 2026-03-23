package com.codexexample.timesheet.api.v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmployeeRequest {
    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email;

    @NotNull(message = "Active flag is required")
    Boolean active;
}
