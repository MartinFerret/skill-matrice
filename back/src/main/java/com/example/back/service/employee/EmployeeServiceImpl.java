package com.example.back.service.employee;

import com.example.back.dto.EmployeeDTO;
import com.example.back.model.Employee;
import com.example.back.model.Role;
import com.example.back.model.Skill;
import com.example.back.repository.EmployeeRepository;
import com.example.back.repository.RoleRepository;
import com.example.back.repository.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, RoleRepository roleRepository, SkillRepository skillRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.skillRepository = skillRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);

        Set<Skill> skills = new HashSet<>();
        if (employeeDTO.getSkillIds() != null) {
            for (Long skillId : employeeDTO.getSkillIds()) {
                Skill skill = skillRepository.findById(skillId)
                        .orElseThrow(() -> new RuntimeException("Skill not found with id: " + skillId));
                skills.add(skill);
            }
        }
        employee.setEmployeeSkills(skills);

        Set<Role> roles = new HashSet<>();
        if (employeeDTO.getRoleIds() != null) {
            for (Long roleId : employeeDTO.getRoleIds()) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
                roles.add(role);
            }
        }
        employee.setRoles(roles);

        return modelMapper.map(employeeRepository.save(employee), EmployeeDTO.class);
    }
}
