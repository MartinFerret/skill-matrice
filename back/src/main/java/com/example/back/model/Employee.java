package com.example.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "employee", cascade = CascadeType.PERSIST)
    private List<Proficiency> proficiencies;

}