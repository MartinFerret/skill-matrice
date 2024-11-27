package com.example.back.dto;

import com.example.back.model.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private String name;
    private SkillLevel skillLevel;
}
