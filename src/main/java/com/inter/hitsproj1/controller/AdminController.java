package com.inter.hitsproj1.controller;

import com.inter.hitsproj1.dto.*;
import com.inter.hitsproj1.service.AdminService;
import com.inter.hitsproj1.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN')")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    @PutMapping(value = "/profile/{userId}", produces = "application/json")
    public ResponseEntity<StatusResponse> changeUserProfile(@RequestBody ProfileChangeDto dto,
                                                            @PathVariable UUID userId,
                                                            Authentication authentication) {
        return userService.changeUserProfile(dto, userId, authentication.getName());
    }

    @PutMapping(value = "/profile", produces = "application/json")
    public ResponseEntity<StatusResponse> changeAdminProfile(@RequestBody AdminChangeDto dto,
                                                             Authentication authentication) {
        return userService.changeAdminProfile(dto, authentication.getName());
    }

    @PostMapping(value = "/create/group", produces = "application/json")
    public ResponseEntity<StatusResponse> createGroup(@Valid @RequestBody GroupDto dto,
                                                      Authentication authentication) {
        return userService.createGroup(dto, authentication.getName());
    }

    @PutMapping(value = "/change/group/{groupId}", produces = "application/json")
    public ResponseEntity<StatusResponse> changeGroup(@Valid @RequestBody GroupDto dto,
                                                      @PathVariable UUID groupId,
                                                      Authentication authentication) {
        return userService.changeGroup(dto, groupId, authentication.getName());
    }

    @PostMapping(value = "/create/faculty", produces = "application/json")
    public ResponseEntity<StatusResponse> createFaculty(@RequestBody FacultyCreateDto dto,
                                                        Authentication authentication) {
        return adminService.createFaculty(dto, authentication.getName());
    }

    @PutMapping(value = "/change/faculty/{facultyId}", produces = "application/json")
    public ResponseEntity<StatusResponse> changeFaculty(@RequestBody FacultyCreateDto dto,
                                                        @PathVariable UUID facultyId,
                                                        Authentication authentication) {
        return adminService.changeFaculty(dto, facultyId, authentication.getName());
    }

    @PostMapping(value = "/create/audience", produces = "application/json")
    public ResponseEntity<StatusResponse> createAuditorium(@Valid @RequestBody AudienceDto dto,
                                                           Authentication authentication) {
        return adminService.createAudience(dto, authentication.getName());
    }

    @PutMapping(value = "/change/audience/{audienceId}", produces = "application/json")
    public ResponseEntity<StatusResponse> changeAuditorium(@Valid @RequestBody AudienceDto dto,
                                                           @PathVariable UUID audienceId,
                                                           Authentication authentication) {
        return adminService.changeAudience(dto, audienceId, authentication.getName());
    }

    @PostMapping(value = "/create/subject", produces = "application/json")
    public ResponseEntity<StatusResponse> createSubject(@Valid @RequestBody SubjectDto dto,
                                                        Authentication authentication) {
        return adminService.createSubject(dto, authentication.getName());
    }

    @PutMapping(value = "/change/subject/{subjectId}", produces = "application/json")
    public ResponseEntity<StatusResponse> changeSubject(@Valid @RequestBody SubjectDto dto,
                                                        @PathVariable UUID subjectId,
                                                        Authentication authentication) {
        return adminService.changeSubject(dto, subjectId, authentication.getName());
    }
}
