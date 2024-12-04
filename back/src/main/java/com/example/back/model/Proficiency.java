package com.example.back.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proficiency")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proficiency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level")
    private SkillLevel skillLevel;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = true)
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = true)
    @JsonBackReference
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = true)
    private Role role;
}
