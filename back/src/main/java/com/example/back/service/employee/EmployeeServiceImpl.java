package com.example.back.service.employee;

import com.example.back.dto.EmployeeDTO;
import com.example.back.dto.ProficiencyEmployeeDTO;
import com.example.back.model.Employee;
import com.example.back.model.Proficiency;
import com.example.back.model.Skill;
import com.example.back.model.SkillLevel;
import com.example.back.repository.EmployeeRepository;
import com.example.back.repository.SkillRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, SkillRepository skillRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.skillRepository = skillRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getProficiencies() == null || employeeDTO.getProficiencies().isEmpty()) {
            throw new IllegalArgumentException("An employee must have at least one proficiency.");
        }

        Employee employee = new Employee();
        employee.setFirstname(employeeDTO.getFirstname());
        employee.setLastname(employeeDTO.getLastname());

        if (employeeDTO.getRole() == null || employeeDTO.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role must not be blank.");
        }
        employee.setRole(employeeDTO.getRole());

        List<Proficiency> proficiencies = new ArrayList<>();

        for (ProficiencyEmployeeDTO proficiencyDTO : employeeDTO.getProficiencies()) {
            Skill skill = skillRepository.findById(proficiencyDTO.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill with ID " + proficiencyDTO.getSkillId() + " not found"));

            Proficiency proficiency = new Proficiency();
            proficiency.setSkillLevel(proficiencyDTO.getSkillLevel());
            proficiency.setSkill(skill);
            proficiency.setEmployee(employee);

            proficiencies.add(proficiency);
        }

        employee.setProficiencies(proficiencies);

        Employee savedEmployee = employeeRepository.save(employee);

        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employee -> {
                    List<Proficiency> proficiencies = employee.getProficiencies();

                    if (proficiencies != null) {
                        proficiencies = proficiencies.stream()
                                .filter(proficiency -> proficiency.getSkill() != null)
                                .collect(Collectors.toList());
                    }

                    List<ProficiencyEmployeeDTO> proficiencyDTOs = proficiencies.stream()
                            .map(proficiency -> {
                                Long skillId = (proficiency.getSkill() != null) ? proficiency.getSkill().getId() : null;
                                return new ProficiencyEmployeeDTO(
                                        SkillLevel.BEGINNER, skillId
                                );
                            })
                            .collect(Collectors.toList());

                    return new EmployeeDTO(
                            employee.getId(),
                            employee.getFirstname(),
                            employee.getLastname(),
                            employee.getRole(),
                            proficiencyDTOs
                    );
                })
                .collect(Collectors.toList());
    }
}
