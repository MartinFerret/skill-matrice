package com.example.back.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_skill_id", referencedColumnName = "id", nullable = true)
    private Skill parentSkill;

    @OneToMany(mappedBy = "skill")
    @JsonManagedReference
    private List<Proficiency> proficiencies;
}