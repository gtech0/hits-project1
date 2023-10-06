package com.inter.hitsproj1.service;

import com.inter.hitsproj1.dto.*;
import com.inter.hitsproj1.entity.*;
import com.inter.hitsproj1.repository.*;
import com.inter.hitsproj1.util.UtilityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final TimeslotRepository timeslotRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final StudentGroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final AudienceRepository audienceRepository;
    private final BookingLessonRepository bookingLessonRepository;

    private static LessonGetDto lessonMapper(LessonEntity lesson) {
        return LessonGetDto.builder()
                .id(lesson.getId())
                .lessonNumber(lesson.getTimeslot().getTimeslotNumber())
                .starts(lesson.getTimeslot().getStartDate())
                .ends(lesson.getTimeslot().getEndDate())
                .lessonType(lesson.getLessonType())
                .groups(lesson.getGroups()
                        .stream()
                        .map(group -> GroupGetDto.builder()
                                .id(group.getId())
                                .name(group.getGroupNumber())
                                .build())
                        .collect(Collectors.toSet())
                )
                .subject(SubjectGetDto.builder()
                        .id(lesson.getSubject().getId())
                        .name(lesson.getSubject().getSubjectName())
                        .build()
                )
                .professor(ProfessorGetDto.builder()
                        .id(lesson.getUser().getId())
                        .fullName(lesson.getUser().getFullName())
                        .build()
                )
                .audience(AudienceGetDto.builder()
                        .id(lesson.getAudience().getId())
                        .name(lesson.getAudience().getAudienceName())
                        .build()
                )
                .build();
    }

    @Transactional
    public ResponseEntity<StatusResponse> createLesson(LessonCreateDto dto, String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        TimeslotEntity timeslot = timeslotRepository.findByTimeslotNumber(dto.getTimeslotNumber());
        AudienceEntity audience = UtilityHelper.findAndCheckAudience(audienceRepository, dto.getAudience());
        SubjectEntity subject = UtilityHelper.findAndCheckSubject(subjectRepository, dto.getSubject());
        UserEntity professor = UtilityHelper.checkRole(dto.getProfessor(), userRepository, RoleEnum.PROFESSOR);

        List<BookingLessonEntity> bookingLessons = bookingLessonRepository
                .findByTimeslotsAndAudienceAndDate(
                        timeslot,
                        audience,
                        LocalDate.parse(dto.getDate())
                );

        if (!bookingLessons.isEmpty()) {
            bookingLessonRepository.deleteAll(bookingLessons);
            log.warn("Intersecting bookings deleted");
        } else {
            log.info("No intersecting bookings found");
        }

        Set<StudentGroupEntity> groups = new HashSet<>();
        dto.getGroups().forEach(
                groupId -> {
                    StudentGroupEntity group = UtilityHelper
                            .findAndCheckGroup(groupRepository, groupId);
                    groups.add(group);
                }
        );
        LessonEntity lesson = LessonEntity.builder()
                .id(UUID.randomUUID())
                .date(LocalDate.parse(dto.getDate()))
                .lessonType(dto.getLessonType())
                .groups(groups)
                .subject(subject)
                .user(professor)
                .audience(audience)
                .timeslot(timeslot)
                .build();
        lessonRepository.save(lesson);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Lesson successfully added")
                .build());
    }

    public ResponseEntity<List<ScheduleDto>> getSchedule(ScheduleTypeEnum type,
                                                         UUID typeId,
                                                         String dateFrom,
                                                         String dateTo) {
        LocalDate startDate = LocalDate.parse(dateFrom);
        LocalDate endDate = LocalDate.parse(dateTo).plusDays(1);

        List<ScheduleDto> schedule = new ArrayList<>();

        List<LocalDate> dates = startDate.datesUntil(endDate).toList();
        for (LocalDate date : dates) {
            List<LessonEntity> lessonsByDate = lessonRepository
                    .findLessonsByDate(date);

            if (!lessonsByDate.isEmpty()) {
                Integer lessonNumber = lessonsByDate.size();
                LocalTime starts = lessonsByDate.get(0).getTimeslot().getStartDate();
                LocalTime ends = lessonsByDate.get(lessonsByDate.size() - 1).getTimeslot().getEndDate();

                List<LessonGetDto> mappedLessonsByDate = lessonsByDate
                        .stream()
                        .filter(lesson -> {
                            if (type == ScheduleTypeEnum.group) {
                                for (StudentGroupEntity group : lesson.getGroups()) {
                                    return Objects.equals(group.getId(), typeId);
                                }
                            } else if (type == ScheduleTypeEnum.audience) {
                                return Objects.equals(lesson.getAudience().getId(), typeId);
                            } else if (type == ScheduleTypeEnum.teacher) {
                                return Objects.equals(lesson.getUser().getId(), typeId);
                            }
                            return false;
                        })
                        .map(ScheduleService::lessonMapper)
                        .toList();

                schedule.add(ScheduleDto.builder()
                        .date(date)
                        .lessons(mappedLessonsByDate)
                        .lessonNumber(lessonNumber)
                        .starts(starts)
                        .ends(ends)
                        .build());
            }
        }

        return ResponseEntity.ok(schedule);
    }

    public ResponseEntity<List<ScheduleDto>> getCombinedSchedule(Set<UUID> groupIds,
                                                                 Set<UUID> audienceIds,
                                                                 Set<UUID> professorIds,
                                                                 String dateFrom,
                                                                 String dateTo) {
        LocalDate startDate = LocalDate.parse(dateFrom);
        LocalDate endDate = LocalDate.parse(dateTo).plusDays(1);

        List<ScheduleDto> schedule = new ArrayList<>();

        List<LocalDate> dates = startDate.datesUntil(endDate).toList();
        for (LocalDate date : dates) {
            List<LessonEntity> lessonsByDate = lessonRepository
                    .findLessonsByDate(date);

            if (!lessonsByDate.isEmpty()) {
                Integer lessonNumber = lessonsByDate.size();
                LocalTime starts = lessonsByDate.get(0).getTimeslot().getStartDate();
                LocalTime ends = lessonsByDate.get(lessonsByDate.size() - 1).getTimeslot().getEndDate();

                List<LessonGetDto> mappedLessonsByDate = lessonsByDate
                        .stream()
                        .filter(lesson -> {
                            boolean audContains = audienceIds == null
                                    || audienceIds.contains(lesson.getAudience().getId());

                            boolean profContains = professorIds == null
                                    || professorIds.contains(lesson.getUser().getId());

                            boolean groupContains = groupIds == null
                                    || groupIds
                                    .stream().anyMatch(lesson.getGroups()
                                            .stream()
                                            .map(StudentGroupEntity::getId)
                                            .collect(Collectors.toSet())::contains
                                    );
                            return audContains && profContains && groupContains;
                        })
                        .map(ScheduleService::lessonMapper)
                        .toList();

                schedule.add(ScheduleDto.builder()
                        .date(date)
                        .lessons(mappedLessonsByDate)
                        .lessonNumber(lessonNumber)
                        .starts(starts)
                        .ends(ends)
                        .build());
            }
        }

        return ResponseEntity.ok(schedule);
    }
}
