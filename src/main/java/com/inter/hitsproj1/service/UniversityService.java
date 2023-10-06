package com.inter.hitsproj1.service;

import com.inter.hitsproj1.dto.*;
import com.inter.hitsproj1.entity.*;
import com.inter.hitsproj1.repository.AudienceRepository;
import com.inter.hitsproj1.repository.FacultyRepository;
import com.inter.hitsproj1.repository.StudentGroupRepository;
import com.inter.hitsproj1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final AudienceRepository audienceRepository;
    private final StudentGroupRepository groupRepository;

    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<StudentGroupEntity> groups = groupRepository.findAll();

        return ResponseEntity.ok(
                groups.stream()
                        .map(group -> new GroupDto(
                                group.getGroupNumber(),
                                group.getUsers()
                                        .stream()
                                        .map(UserEntity::getId)
                                        .collect(Collectors.toSet())))
                        .collect(Collectors.toList())
        );
    }

    public ResponseEntity<List<ProfessorDto>> getAllProfessors() {
        List<UserEntity> users = userRepository.findAll();

        return ResponseEntity.ok(
                users.stream()
                        .filter(user -> user.getRoles().contains(RoleEnum.PROFESSOR))
                        .map(user -> new ProfessorDto(
                                user.getId(),
                                user.getFullName()))
                        .collect(Collectors.toList())
        );
    }

    public ResponseEntity<List<FacultyGetDto>> getAllFaculties() {
        List<FacultyEntity> faculties = facultyRepository.findAll();

        return ResponseEntity.ok(
                faculties.stream()
                        .map(faculty -> new FacultyGetDto(
                                faculty.getId(),
                                faculty.getFacultyName()))
                        .collect(Collectors.toList())
        );
    }

    public ResponseEntity<List<AudienceGetDto>> getAllAudiences() {
        List<AudienceEntity> auditoriums = audienceRepository.findAll();

        return ResponseEntity.ok(
                auditoriums.stream()
                        .map(auditorium -> new AudienceGetDto(
                                auditorium.getId(),
                                auditorium.getAudienceName()))
                        .collect(Collectors.toList())
        );
    }

    public ResponseEntity<List<GroupGetDto>> getAllFacultyGroups(UUID facultyId) {
        Set<StudentGroupEntity> groups = groupRepository.findAllByFaculty_Id(facultyId);

        return ResponseEntity.ok(
                groups.stream()
                        .map(group -> new GroupGetDto(
                                group.getId(),
                                group.getGroupNumber()))
                        .collect(Collectors.toList())
        );
    }
}
