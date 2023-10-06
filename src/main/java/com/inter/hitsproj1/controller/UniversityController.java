package com.inter.hitsproj1.controller;

import com.inter.hitsproj1.dto.*;
import com.inter.hitsproj1.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/university")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping(value = "/groups", produces = "application/json")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        return universityService.getAllGroups();
    }

    @GetMapping(value = "/professors", produces = "application/json")
    public ResponseEntity<List<ProfessorDto>> getAllProfessors() {
        return universityService.getAllProfessors();
    }

    @GetMapping(value = "/faculties", produces = "application/json")
    public ResponseEntity<List<FacultyGetDto>> getAllFaculties() {
        return universityService.getAllFaculties();
    }

    @GetMapping(value = "/audiences", produces = "application/json")
    public ResponseEntity<List<AudienceGetDto>> getAllAudiences() {
        return universityService.getAllAudiences();
    }

    @GetMapping(value = "/faculties/{facultyId}/groups", produces = "application/json")
    public ResponseEntity<List<GroupGetDto>> getAllFacultyGroups(@PathVariable UUID facultyId) {
        return universityService.getAllFacultyGroups(facultyId);
    }
}
