package com.example.back.dto;

import com.example.back.model.Skill;
import com.example.back.model.SkillLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProficiencyDTO {
    @JsonIgnore
    private Long id;
    private SkillDTO skill;
    private SkillLevel skillLevel;
}
