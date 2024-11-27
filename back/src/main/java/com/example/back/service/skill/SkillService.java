package com.example.back.service.skill;

import com.example.back.dto.SkillDTO;
import com.example.back.model.Skill;

import java.util.List;

public interface SkillService {
    List<Skill> getAllSkills();
    SkillDTO createSkill(SkillDTO skillDTO);
    Skill getSkillById(Long id);
    SkillDTO updateSkill(Long id, SkillDTO skillDTO);
    void deleteSkill(Long id);
}

