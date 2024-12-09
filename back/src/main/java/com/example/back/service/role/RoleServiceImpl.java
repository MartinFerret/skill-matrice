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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        return roles.stream().map(role -> {
            RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);

            List<ProficiencyDTO> proficiencies = (role.getProficiencies() != null) ?
                    role.getProficiencies().stream().map(proficiency -> {
                        ProficiencyDTO proficiencyDTO = new ProficiencyDTO();
                        proficiencyDTO.setId(proficiency.getId());
                        proficiencyDTO.setSkillLevel(proficiency.getSkillLevel());
                        proficiencyDTO.setSkillName(proficiency.getSkill().getName());
                        proficiencyDTO.setSkillId(proficiency.getSkill().getId());
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
                .map(ProficiencyDTO::getSkillId)
                .collect(Collectors.toList());
        if (skillIds.isEmpty()) {
            throw new RuntimeException("Aucune compétence n'a été fournie pour ce rôle");
        }

        for (Long skillId : skillIds) {
            Skill skill = skillRepository.findById(skillId)
                    .orElseThrow(() -> new RuntimeException("Compétence non trouvée avec l'ID : " + skillId));

            Proficiency proficiency = new Proficiency();
            proficiency.setSkill(skill);
            proficiency.setRole(savedRole);
            proficiency.setSkillLevel(roleDTO.getProficiencies().get(0).getSkillLevel());
            proficiencyRepository.save(proficiency);
            proficiencies.add(proficiency);
        }

        List<ProficiencyDTO> proficiencyDTOs = proficiencies.stream()
                .map(p -> new ProficiencyDTO(p.getId(), p.getSkill().getName(), p.getSkillLevel(), p.getSkill().getId()))
                .collect(Collectors.toList());

        return new RoleDTO(savedRole.getId(), savedRole.getName(), proficiencyDTOs);
    }
}
