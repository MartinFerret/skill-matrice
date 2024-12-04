package com.example.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String role;
    private List<ProficiencyEmployeeDTO> proficiencies;

    public EmployeeDTO(Long id, String firstname, String lastname, List<ProficiencyEmployeeDTO> proficiencies) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.proficiencies = proficiencies;
    }
}
