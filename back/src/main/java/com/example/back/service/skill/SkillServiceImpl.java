package com.example.back.service.skill;

import com.example.back.dto.SkillDTO;
import com.example.back.model.Skill;
import com.example.back.repository.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    public SkillServiceImpl(SkillRepository skillRepository, ModelMapper modelMapper) {
        this.skillRepository = skillRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        Skill skill = modelMapper.map(skillDTO, Skill.class);
        Skill savedSkill = skillRepository.save(skill);
        return modelMapper.map(savedSkill, SkillDTO.class);
    }

    @Override
    public Skill getSkillById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    @Override
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        Skill skillStored = skillRepository.findById(id).orElse(null);
        if (skillStored == null) {
            throw new RuntimeException("Skill not found");
        }

        skillStored.setName(skillDTO.getName());
        skillStored.setSkillLevel(skillDTO.getSkillLevel());
        skillRepository.save(skillStored);

        return modelMapper.map(skillStored, SkillDTO.class);
    }

    @Override
    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id).orElse(null);
        if (skill == null) {
            throw new RuntimeException("Skill not found");
        }
        skillRepository.delete(skill);
    }
}
