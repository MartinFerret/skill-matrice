package com.example.back.service.role;

import com.example.back.dto.ProficiencyDTO;
import com.example.back.dto.RoleDTO;
import com.example.back.model.Proficiency;
import com.example.back.model.Role;
import com.example.back.model.Skill;
import com.example.back.model.SkillLevel;
import com.example.back.repository.ProficiencyRepository;
import com.example.back.repository.RoleRepository;
import com.example.back.repository.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final SkillRepository skillRepository;
    private final ProficiencyRepository proficiencyRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, SkillRepository skillRepository, ProficiencyRepository proficiencyRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.skillRepository = skillRepository;
        this.proficiencyRepository = proficiencyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        // Transformer les rôles en RoleDTOs
        return roles.stream().map(role -> {
            RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);

            // Transformer les proficiencies pour inclure seulement skillName et skillLevel
            List<ProficiencyDTO> proficiencies = role.getProficiencies().stream().map(proficiency -> {
                ProficiencyDTO proficiencyDTO = new ProficiencyDTO();
                proficiencyDTO.setId(proficiency.getId());
                proficiencyDTO.setSkillLevel(proficiency.getSkillLevel());
                proficiencyDTO.setSkillName(proficiency.getSkill().getName()); // Ajouter skillName
                return proficiencyDTO;
            }).collect(java.util.stream.Collectors.toList()); // Utiliser Collectors.toList()

            roleDTO.setProficiencies(proficiencies);
            return roleDTO;
        }).collect(java.util.stream.Collectors.toList()); // Utiliser Collectors.toList()
    }


    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);

        Role savedRole = roleRepository.save(role);

        List<Proficiency> proficiencies = new ArrayList<>();
        for (Long skillId : roleDTO.getSkillIds()) {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new RuntimeException("Skill not found for id: " + skillId));

            Proficiency proficiency = new Proficiency();
            proficiency.setRole(savedRole);
            proficiency.setSkill(skill);
            proficiency.setSkillLevel(SkillLevel.BEGINNER);

            proficiencies.add(proficiency);
        }

        proficiencyRepository.saveAll(proficiencies);

        savedRole.setProficiencies(proficiencies);

        return modelMapper.map(savedRole, RoleDTO.class);
    }
}
