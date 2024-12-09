package com.example.back.dto;

import com.example.back.model.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProficiencyEmployeeDTO {
    private SkillLevel skillLevel;
    private Long skillId;
    private String skillName;
}
