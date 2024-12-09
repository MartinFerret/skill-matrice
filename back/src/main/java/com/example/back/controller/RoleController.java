package com.example.back.controller;

import com.example.back.dto.RoleDTO;
import com.example.back.service.role.RoleService;
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

    @PostMapping
    public ResponseEntity<List<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<RoleDTO>> deleteById (@PathVariable("id") Long id) {
        roleService.deleteRoleById(id);
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

}
