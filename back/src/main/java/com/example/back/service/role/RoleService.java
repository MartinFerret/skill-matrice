package com.example.back.service.role;

import com.example.back.dto.RoleDTO;
import com.example.back.model.Role;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    List<RoleDTO> getAllRoles();
}

