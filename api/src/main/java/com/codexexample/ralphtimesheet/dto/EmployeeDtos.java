package com.codexexample.ralphtimesheet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
}

@Data
public class CreateEmployeeRequest {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
