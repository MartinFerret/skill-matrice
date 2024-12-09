package com.example.back.repository;

import com.example.back.model.Proficiency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProficiencyRepository extends JpaRepository<Proficiency, Long> {
    void deleteByRoleId(Long roleId);
    void deleteEmployeeById(Long employeeId);
}
