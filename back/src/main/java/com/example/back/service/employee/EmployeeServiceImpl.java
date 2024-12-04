package com.example.back.service.employee;

import com.example.back.repository.EmployeeRepository;
import com.example.back.repository.ProficiencyRepository;
import com.example.back.repository.RoleRepository;
import com.example.back.repository.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProficiencyRepository proficiencyRepository;
    private final SkillRepository skillRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ProficiencyRepository proficiencyRepository, SkillRepository skillRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.proficiencyRepository = proficiencyRepository;
        this.skillRepository = skillRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }
}
