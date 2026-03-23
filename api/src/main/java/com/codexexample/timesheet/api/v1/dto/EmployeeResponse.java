package com.codexexample.timesheet.api.v1.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class EmployeeResponse {
    UUID id;
    String firstName;
    String lastName;
    String email;
    Boolean active;
}
