package com.example.back.service.role;

import com.example.back.dto.RoleDTO;
import com.example.back.model.Role;
import com.example.back.model.Skill;
import com.example.back.repository.RoleRepository;
import com.example.back.repository.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final SkillRepository skillRepository;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper, SkillRepository skillRepository) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);

        List<Skill> skillsList = skillRepository.findAllById(roleDTO.getSkillIds());

        Set<Skill> skillsSet = new HashSet<>(skillsList);
        role.setRoleSkills(skillsSet);

        Role savedRole = roleRepository.save(role);

        return modelMapper.map(savedRole, RoleDTO.class);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id).orElse(null);
        if (role == null) {
            throw new RuntimeException("Role not found");
        }

        role.setName(roleDTO.getName());

        Set<Skill> skills = new HashSet<>();
        if (roleDTO.getSkillIds() != null && !roleDTO.getSkillIds().isEmpty()) {
            skills = roleDTO.getSkillIds().stream()
                    .map(skillId -> skillRepository.findById(skillId)
                            .orElseThrow(() -> new RuntimeException("Skill not found for id: " + skillId)))
                    .collect(Collectors.toSet());
        }

        role.setRoleSkills(skills);

        roleRepository.save(role);

        return modelMapper.map(role, RoleDTO.class);
    }
}
