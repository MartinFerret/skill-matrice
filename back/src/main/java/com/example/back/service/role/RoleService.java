package com.example.back.service.role;

import com.example.back.dto.RoleDTO;
import com.example.back.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    RoleDTO createRole(RoleDTO roleDTO);
    Role getRoleById(Long id);
    void deleteRoleById(Long id);
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
}

