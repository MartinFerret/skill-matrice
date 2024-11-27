package com.example.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "firstname")
    private String firstname;

    @NotBlank
    @Size(max = 20)
    @Column(name = "lastname")
    private String lastname;

    @ManyToMany
    @JoinTable(
            name = "employee_skill_proficiency",
            joinColumns = @JoinColumn(name = "employee_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "skill_id", nullable = true)
    )
    private Set<Skill> employeeSkills;

    @ManyToMany
    @JoinTable(
            name = "employee_role_proficiency",
            joinColumns = @JoinColumn(name = "employee_id", nullable = true),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = true)
    )
    private Set<Role> roles;

}