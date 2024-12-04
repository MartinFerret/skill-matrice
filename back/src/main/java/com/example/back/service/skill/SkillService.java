package com.example.back.service.skill;

import com.example.back.dto.SkillDTO;

import java.util.List;

public interface SkillService {
    SkillDTO createSkill(SkillDTO skillDTO);
    List<SkillDTO> getAllSkills();
    SkillDTO getSkillById(Long id);
    void deleteSkillById(Long id);
    SkillDTO updateSkillById(Long id, SkillDTO skillDTO);
}

