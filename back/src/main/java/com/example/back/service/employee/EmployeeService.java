package com.example.back.service.employee;

import com.example.back.dto.EmployeeDTO;
import com.example.back.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
}

