package com.inter.hitsproj1.service;

import com.inter.hitsproj1.dto.AudienceDto;
import com.inter.hitsproj1.dto.FacultyCreateDto;
import com.inter.hitsproj1.dto.StatusResponse;
import com.inter.hitsproj1.dto.SubjectDto;
import com.inter.hitsproj1.entity.*;
import com.inter.hitsproj1.repository.*;
import com.inter.hitsproj1.util.UtilityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final FacultyRepository facultyRepository;
    private final StudentGroupRepository groupRepository;
    private final AudienceRepository audienceRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public ResponseEntity<StatusResponse> createFaculty(FacultyCreateDto dto, String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        FacultyEntity faculty = FacultyEntity.builder()
                .id(UUID.randomUUID())
                .facultyName(dto.getFacultyName())
                .build();
        facultyRepository.save(faculty);

        Set<StudentGroupEntity> groups = new HashSet<>();
        dto.getGroups().forEach(
                groupId -> {
                    StudentGroupEntity group = UtilityHelper.findAndCheckGroup(groupRepository, groupId);
                    group.setFaculty(faculty);
                    groups.add(group);
                }
        );

        Set<UserEntity> professors = new HashSet<>();
        dto.getProfessors().forEach(
                professorId -> {
                    UserEntity professor = UtilityHelper
                            .checkRole(professorId, userRepository, RoleEnum.PROFESSOR);
                    professor.setFaculty(faculty);
                    professors.add(professor);
                }
        );
        faculty.setUsers(professors);
        faculty.setGroups(groups);
        facultyRepository.save(faculty);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Faculty created")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> changeFaculty(FacultyCreateDto dto,
                                                        UUID facultyId,
                                                        String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        FacultyEntity faculty = UtilityHelper.findAndCheckFaculty(facultyRepository, facultyId);

        faculty.getGroups()
                .stream()
                .filter(group -> !dto.getGroups().contains(group.getId()))
                .forEach(group -> {
                    group.setFaculty(null);
                    groupRepository.save(group);
                });

        Set<StudentGroupEntity> groups = dto.getGroups()
                .stream()
                .map(groupId -> {
                    StudentGroupEntity group = UtilityHelper
                            .findAndCheckGroup(groupRepository, groupId);
                    group.setFaculty(faculty);
                    return group;
                })
                .collect(Collectors.toSet());

        faculty.getUsers()
                .stream()
                .filter(user -> !dto.getProfessors().contains(user.getId()))
                .forEach(user -> {
                    user.setFaculty(null);
                    userRepository.save(user);
                });

        Set<UserEntity> professors = dto.getProfessors()
                .stream()
                .map(professorId -> {
                    UserEntity professor = UtilityHelper
                            .checkRole(professorId, userRepository, RoleEnum.PROFESSOR);
                    professor.setFaculty(faculty);
                    return professor;
                })
                .collect(Collectors.toSet());

        faculty.setFacultyName(dto.getFacultyName());
        faculty.setGroups(groups);
        faculty.setUsers(professors);
        facultyRepository.save(faculty);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Faculty updated")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> createAudience(AudienceDto dto, String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        AudienceEntity auditorium = AudienceEntity.builder()
                .id(UUID.randomUUID())
                .audienceName(dto.getAudienceName())
                .build();
        audienceRepository.save(auditorium);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Audience created")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> changeAudience(AudienceDto dto,
                                                         UUID auditoriumId,
                                                         String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        AudienceEntity auditorium = UtilityHelper
                .findAndCheckAudience(audienceRepository, auditoriumId);
        auditorium.setAudienceName(dto.getAudienceName());
        audienceRepository.save(auditorium);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Audience updated")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> createSubject(SubjectDto dto, String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        SubjectEntity subject = SubjectEntity.builder()
                .id(UUID.randomUUID())
                .subjectName(dto.getSubjectName())
                .build();
        subjectRepository.save(subject);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Subject created")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> changeSubject(SubjectDto dto,
                                                        UUID subjectId,
                                                        String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        SubjectEntity subject = UtilityHelper
                .findAndCheckSubject(subjectRepository, subjectId);
        subject.setSubjectName(dto.getSubjectName());
        subjectRepository.save(subject);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Subject updated")
                .build());
    }
}
