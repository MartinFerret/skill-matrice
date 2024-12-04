package com.example.back.service.skill;

import com.example.back.dto.SkillDTO;
import com.example.back.model.Skill;
import com.example.back.repository.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private final ModelMapper modelMapper;
    private final SkillRepository skillRepository;

    public SkillServiceImpl(ModelMapper modelMapper, SkillRepository skillRepository) {
        this.modelMapper = modelMapper;
        this.skillRepository = skillRepository;
    }

    @Override
    public SkillDTO createSkill(SkillDTO skillDTO) {
        Skill skill = modelMapper.map(skillDTO, Skill.class);

        Skill savedSkill = skillRepository.save(skill);

        return modelMapper.map(savedSkill, SkillDTO.class);
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skill -> modelMapper.map(skill, SkillDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SkillDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
        return modelMapper.map(skill, SkillDTO.class);
    }

    @Override
    public void deleteSkillById(Long id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
        skillRepository.delete(skill);
    }

    @Override
    public SkillDTO updateSkillById(Long id, SkillDTO skillDTO) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        existingSkill.setName(skillDTO.getName());

        Skill updatedSkill = skillRepository.save(existingSkill);

        return modelMapper.map(updatedSkill, SkillDTO.class);
    }
}
