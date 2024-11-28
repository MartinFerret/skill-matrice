package com.example.back.controller;

import com.example.back.dto.RoleDTO;
import com.example.back.dto.SkillDTO;
import com.example.back.model.Role;
import com.example.back.service.role.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable("roleId") Long roleId) {
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO roleCreated = roleService.createRole(roleDTO);
            return new ResponseEntity<>(roleCreated,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> createRole(@PathVariable("roleId") Long roleId) {
        roleService.deleteRoleById(roleId);
        return ResponseEntity.noContent().build();
    }
}
