package com.inter.hitsproj1.util;

import com.inter.hitsproj1.dto.AudienceGetDto;
import com.inter.hitsproj1.dto.BookingGetDto;
import com.inter.hitsproj1.dto.BookingTimeslotGetDto;
import com.inter.hitsproj1.dto.BookingUserGetDto;
import com.inter.hitsproj1.entity.*;
import com.inter.hitsproj1.exception.NotFoundException;
import com.inter.hitsproj1.exception.PermissionDeniedException;
import com.inter.hitsproj1.repository.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@UtilityClass
public class UtilityHelper {

    public UserEntity checkRole(UUID studentId,
                                UserRepository userRepository,
                                RoleEnum role) {
        Optional<UserEntity> user = userRepository.findById(studentId);
        if (user.isEmpty()) {
            log.info("User doesn't exist");
            throw new NotFoundException("User doesn't exist");
        } else if (!user.get().getRoles().contains(role)) {
            throw new PermissionDeniedException("This user is not a " + role.name());
        }
        return user.get();
    }

    public UserEntity findAndCheckUser(UserRepository userRepository,
                                       String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User doesn't exist");
                    return new NotFoundException("User doesn't exist");
                });
    }

    public StudentGroupEntity findAndCheckGroup(StudentGroupRepository groupRepository,
                                                UUID groupId) {
        return groupRepository
                .findById(groupId)
                .orElseThrow(() -> {
                    log.error("Group doesn't exist");
                    return new NotFoundException("Group doesn't exist");
                });
    }

    public AudienceEntity findAndCheckAudience(AudienceRepository audienceRepository,
                                               UUID auditoriumId) {
        return audienceRepository
                .findById(auditoriumId)
                .orElseThrow(() -> {
                    log.error("Audience doesn't exist");
                    return new NotFoundException("Audience doesn't exist");
                });
    }

    public SubjectEntity findAndCheckSubject(SubjectRepository subjectRepository,
                                             UUID subjectId) {
        return subjectRepository
                .findById(subjectId)
                .orElseThrow(() -> {
                    log.error("Group doesn't exist");
                    return new NotFoundException("Group doesn't exist");
                });
    }

    public FacultyEntity findAndCheckFaculty(FacultyRepository facultyRepository,
                                             UUID subjectId) {
        return facultyRepository
                .findById(subjectId)
                .orElseThrow(() -> {
                    log.error("Faculty doesn't exist");
                    return new NotFoundException("Faculty doesn't exist");
                });
    }

    public static BookingGetDto bookingMapping(BookingLessonEntity bookedLesson) {
        return BookingGetDto.builder()
                .id(bookedLesson.getId())
                .title(bookedLesson.getTitle())
                .participationCount(bookedLesson.getParticipationCount())
                .date(bookedLesson.getDate())
                .audience(AudienceGetDto.builder()
                        .id(bookedLesson.getAudience().getId())
                        .name(bookedLesson.getAudience().getAudienceName())
                        .build())
                .user(BookingUserGetDto.builder()
                        .id(bookedLesson.getUser().getId())
                        .email(bookedLesson.getUser().getEmail())
                        .fullName(bookedLesson.getUser().getFullName())
                        .roles(bookedLesson.getUser().getRoles())
                        .build())
                .lessons(bookedLesson.getTimeslots()
                        .stream()
                        .map(timeslot -> BookingTimeslotGetDto.builder()
                                .id(timeslot.getId())
                                .lessonNumber(timeslot.getTimeslotNumber())
                                .start(timeslot.getStartDate())
                                .end(timeslot.getEndDate())
                                .build())
                        .toList())
                .build();
    }
}
