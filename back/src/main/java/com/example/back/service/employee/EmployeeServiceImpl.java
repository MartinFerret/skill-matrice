package com.example.back.service.employee;

import com.example.back.dto.EmployeeDTO;
import com.example.back.dto.ProficiencyEmployeeDTO;
import com.example.back.model.Employee;
import com.example.back.model.Proficiency;
import com.example.back.model.Skill;
import com.example.back.repository.EmployeeRepository;
import com.example.back.repository.ProficiencyRepository;
import com.example.back.repository.SkillRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final SkillRepository skillRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final ProficiencyRepository proficiencyRepository;

    public EmployeeServiceImpl(SkillRepository skillRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper, ProficiencyRepository proficiencyRepository) {
        this.skillRepository = skillRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.proficiencyRepository = proficiencyRepository;
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getProficiencies() == null) {
            throw new IllegalArgumentException("An employee must have at least one proficiency.");
        }

        Employee employee = new Employee();
        employee.setFirstname(employeeDTO.getFirstname());
        employee.setLastname(employeeDTO.getLastname());

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
                                Skill skill = proficiency.getSkill();
                                Long skillId = (skill != null) ? skill.getId() : null;
                                String skillName = (skill != null) ? skill.getName() : null;

                                return new ProficiencyEmployeeDTO(
                                        proficiency.getSkillLevel(), skillId, skillName
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

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        proficiencyRepository.deleteEmployeeById(id);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));
        employeeRepository.delete(employee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Proficiency> proficiencies = employee.getProficiencies();

        if (proficiencies != null) {
            proficiencies = proficiencies.stream()
                    .filter(proficiency -> proficiency.getSkill() != null)
                    .collect(Collectors.toList());
        }

        List<ProficiencyEmployeeDTO> proficiencyDTOs = proficiencies.stream()
                .map(proficiency -> {
                    Skill skill = proficiency.getSkill();
                    Long skillId = (skill != null) ? skill.getId() : null;
                    String skillName = (skill != null) ? skill.getName() : null;

                    return new ProficiencyEmployeeDTO(
                            proficiency.getSkillLevel(), skillId, skillName
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
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found"));

        employee.setFirstname(employeeDTO.getFirstname());
        employee.setLastname(employeeDTO.getLastname());
        employee.setRole(employeeDTO.getRole());

        if (employeeDTO.getProficiencies() != null) {
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
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }
}
