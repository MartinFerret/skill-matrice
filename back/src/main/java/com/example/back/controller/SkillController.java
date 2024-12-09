package com.example.back.controller;

import com.example.back.dto.SkillDTO;
import com.example.back.service.skill.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<List<SkillDTO>> createSkill(@Valid @RequestBody SkillDTO skillDTO) {
        skillService.createSkill(skillDTO);
        return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDTO> getSkillById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(skillService.getSkillById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<SkillDTO>> deleteSKillById(@PathVariable("id") Long id) {
        skillService.deleteSkillById(id);
        return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillDTO> updateSkillById(@PathVariable("id") Long id, @Valid @RequestBody SkillDTO skillDTO) {
        return new ResponseEntity<>(skillService.updateSkillById(id, skillDTO), HttpStatus.OK);
    }
}
