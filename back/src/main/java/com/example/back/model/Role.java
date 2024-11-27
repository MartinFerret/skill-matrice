package com.example.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "proficiencies_for_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonIgnore
    private Set<Skill> roleSkills;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<Employee> employees;
}
