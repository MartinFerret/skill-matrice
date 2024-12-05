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

        if (skillDTO.getParentSkillId() != null) {
            Skill parentSkill = skillRepository.findById(skillDTO.getParentSkillId())
                    .orElse(null);

            if (parentSkill != null) {
                skill.setParentSkill(parentSkill);

                skillDTO.setParentName(parentSkill.getName());
            } else {
                skillDTO.setParentName("Parent Skill not found");
            }
        } else {
            skillDTO.setParentName("No parent skill");
        }

        Skill savedSkill = skillRepository.save(skill);

        SkillDTO skillDTOResult = modelMapper.map(savedSkill, SkillDTO.class);

        skillDTOResult.setParentName(skillDTO.getParentName());

        return skillDTOResult;
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(skill -> {
                    SkillDTO skillDTO = modelMapper.map(skill, SkillDTO.class);

                    if (skill.getParentSkill() != null) {
                        skillDTO.setParentName(skill.getParentSkill().getName());
                    } else {
                        skillDTO.setParentName(null);
                    }

                    return skillDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SkillDTO getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        SkillDTO skillDTO = modelMapper.map(skill, SkillDTO.class);

        if (skill.getParentSkill() != null) {
            skillDTO.setParentName(skill.getParentSkill().getName());
        } else {
            skillDTO.setParentName(null);
        }

        return skillDTO;
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

        if (skillDTO.getParentSkillId() != null) {
            Skill parentSkill = skillRepository.findById(skillDTO.getParentSkillId())
                    .orElseThrow(() -> new RuntimeException("Parent Skill not found"));
            existingSkill.setParentSkill(parentSkill);
        } else {
            existingSkill.setParentSkill(null);
        }

        Skill updatedSkill = skillRepository.save(existingSkill);

        SkillDTO updatedSkillDTO = modelMapper.map(updatedSkill, SkillDTO.class);

        if (updatedSkill.getParentSkill() != null) {
            updatedSkillDTO.setParentName(updatedSkill.getParentSkill().getName());
        } else {
            updatedSkillDTO.setParentName(null);
        }

        return updatedSkillDTO;
    }
}
