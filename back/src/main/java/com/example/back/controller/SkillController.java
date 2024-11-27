package com.example.back.controller;

import com.example.back.dto.SkillDTO;
import com.example.back.model.Skill;
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

    @GetMapping("")
    public ResponseEntity<List<Skill>> getSkills() {
        return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.OK);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<Skill> getSkillById(@PathVariable("skillId") Long skillId) {
        return new ResponseEntity<>(skillService.getSkillById(skillId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<SkillDTO> createSkill(@Valid @RequestBody SkillDTO skillDTO) {
        try {
            SkillDTO skillCreated = skillService.createSkill(skillDTO);
            return new ResponseEntity<>(skillCreated,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<SkillDTO> updateSkill(@PathVariable("skillId") Long skillId, @Valid @RequestBody SkillDTO skillDTO) {
        SkillDTO skillUpdated = skillService.updateSkill(skillId, skillDTO);
        return new ResponseEntity<>(skillUpdated,HttpStatus.OK);
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(@PathVariable("skillId") Long skillId) {
        skillService.getSkillById(skillId);
        return ResponseEntity.noContent().build();
    }
}
