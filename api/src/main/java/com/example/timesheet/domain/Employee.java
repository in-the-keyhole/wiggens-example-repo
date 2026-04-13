package com.example.timesheet.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;
}
