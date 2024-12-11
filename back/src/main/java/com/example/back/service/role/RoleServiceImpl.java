package com.example.back.service.role;

import com.example.back.dto.ProficiencyDTO;
import com.example.back.dto.RoleDTO;
import com.example.back.dto.SkillDTO;
import com.example.back.model.Proficiency;
import com.example.back.model.Role;
import com.example.back.model.Skill;
import com.example.back.repository.ProficiencyRepository;
import com.example.back.repository.RoleRepository;
import com.example.back.repository.SkillRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final SkillRepository skillRepository;
    private final ProficiencyRepository proficiencyRepository;

    public RoleServiceImpl(RoleRepository roleRepository, SkillRepository skillRepository, ProficiencyRepository proficiencyRepository) {
        this.roleRepository = roleRepository;
        this.skillRepository = skillRepository;
        this.proficiencyRepository = proficiencyRepository;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(role -> {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setName(role.getName());

            List<ProficiencyDTO> proficiencies = (role.getProficiencies() != null) ?
                    role.getProficiencies().stream().map(proficiency -> {
                        ProficiencyDTO proficiencyDTO = new ProficiencyDTO();
                        proficiencyDTO.setId(proficiency.getId());
                        proficiencyDTO.setSkillLevel(proficiency.getSkillLevel());

                        Skill skill = proficiency.getSkill();
                        SkillDTO skillDTO = new SkillDTO();
                        skillDTO.setId(skill.getId());
                        skillDTO.setName(skill.getName());
                        skillDTO.setParentSkillId(skill.getParentSkill() != null ? skill.getParentSkill().getId() : null);
                        skillDTO.setParentName(skill.getParentSkill() != null ? skill.getParentSkill().getName() : null);

                        proficiencyDTO.setSkill(skillDTO);

                        return proficiencyDTO;
                    }).collect(java.util.stream.Collectors.toList()) : new ArrayList<>();

            roleDTO.setProficiencies(proficiencies);

            return roleDTO;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        proficiencyRepository.deleteByRoleId(roleId);

        roleRepository.delete(role);
    }


    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleDTO.getName() == null || roleDTO.getName().isEmpty()) {
            throw new RuntimeException("Le nom du rôle ne peut pas être vide");
        }

        Role role = new Role();
        role.setName(roleDTO.getName());
        Role savedRole = roleRepository.save(role);

        List<Proficiency> proficiencies = new ArrayList<>();
        List<Long> skillIds = roleDTO.getProficiencies().stream()
                .map(proficiencyDTO -> proficiencyDTO.getSkill().getId())
                .collect(Collectors.toList());

        for (int i = 0; i < skillIds.size(); i++) {
            Long skillId = skillIds.get(i);
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new RuntimeException("Compétence non trouvée avec l'ID : " + skillId));

            Proficiency proficiency = new Proficiency();
            proficiency.setSkill(skill);
            proficiency.setRole(savedRole);

            proficiency.setSkillLevel(roleDTO.getProficiencies().get(i).getSkillLevel());

            proficiencyRepository.save(proficiency);
            proficiencies.add(proficiency);
        }

        List<ProficiencyDTO> proficiencyDTOs = proficiencies.stream()
                .map(proficiency -> {
                    ProficiencyDTO proficiencyDTO = new ProficiencyDTO();
                    proficiencyDTO.setId(proficiency.getId());

                    SkillDTO skillDTO = new SkillDTO();
                    skillDTO.setId(proficiency.getSkill().getId());
                    skillDTO.setName(proficiency.getSkill().getName());
                    skillDTO.setParentSkillId(proficiency.getSkill().getParentSkill() != null ? proficiency.getSkill().getParentSkill().getId() : null);

                    proficiencyDTO.setSkill(skillDTO);
                    proficiencyDTO.setSkillLevel(proficiency.getSkillLevel());

                    return proficiencyDTO;
                })
                .collect(Collectors.toList());

        return new RoleDTO(savedRole.getId(), savedRole.getName(), proficiencyDTOs);
    }
}
