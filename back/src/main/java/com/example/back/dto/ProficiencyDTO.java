package com.example.back.dto;

import com.example.back.model.SkillLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProficiencyDTO {
    @JsonIgnore
    private Long id;
    private String skillName;
    private SkillLevel skillLevel;
    @JsonIgnore
    private Long skillId;
    private Long roleId;
}
