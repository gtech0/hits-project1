package com.inter.hitsproj1.service;

import com.inter.hitsproj1.dto.*;
import com.inter.hitsproj1.entity.*;
import com.inter.hitsproj1.exception.NotFoundException;
import com.inter.hitsproj1.exception.UniqueConstraintViolationException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepository;
    private final TimeslotRepository timeslotRepository;
    private final BookingLessonRepository bookingLessonRepository;
    private final AudienceRepository audienceRepository;
    private final LessonRepository lessonRepository;

    public ResponseEntity<List<BookingGetDto>> getBookings(String email) {
        UserEntity user = UtilityHelper.findAndCheckUser(userRepository, email);

        List<BookingGetDto> booking = bookingLessonRepository
                .findAllByUser(user)
                .stream()
                .map(UtilityHelper::bookingMapping)
                .toList();

        return ResponseEntity.ok(booking);
    }

    @Transactional
    public ResponseEntity<StatusResponse> bookPlace(BookingCreateDto dto, String email) {

        UserEntity user = UtilityHelper.findAndCheckUser(userRepository, email);

        AudienceEntity audience = UtilityHelper
                .findAndCheckAudience(audienceRepository, dto.getAudienceId());

        Set<TimeslotEntity> timeslots = new HashSet<>();
        for (TimeslotGetDto timeslotGetDto : dto.getLessons()) {
            TimeslotEntity timeslot = timeslotRepository
                    .findByTimeslotNumberAndStartDateAndEndDate(
                            timeslotGetDto.getLessonNumber(),
                            LocalTime.parse(timeslotGetDto.getStart()),
                            LocalTime.parse(timeslotGetDto.getEnd())
                    )
                    .orElseThrow(() -> {
                        log.error("Timeslot doesn't exist");
                        return new NotFoundException("Timeslot doesn't exist");
                    });

            List<BookingLessonEntity> bookingLessons = bookingLessonRepository
                    .findByTimeslotsAndAudienceAndDate(
                            timeslot,
                            audience,
                            LocalDate.parse(dto.getDate())
                    );

            if (!bookingLessons.isEmpty()) {
                log.warn("Intersecting bookings detected");
                throw new UniqueConstraintViolationException("Intersecting bookings deleted");
            } else {
                log.info("No intersecting bookings found");
            }

            List<LessonEntity> lessons = lessonRepository
                    .findAllByAudienceAndDateAndTimeslot(
                            audience,
                            LocalDate.parse(dto.getDate()),
                            timeslot
                    );

            if (!lessons.isEmpty()) {
                log.warn("Intersecting lessons detected");
                throw new UniqueConstraintViolationException("Intersecting lessons deleted");
            } else {
                log.info("No intersecting lessons detected");
            }

            timeslots.add(timeslot);
        }

        BookingLessonEntity bookingLesson = BookingLessonEntity.builder()
                .id(UUID.randomUUID())
                .audience(audience)
                .user(user)
                .date(LocalDate.parse(dto.getDate()))
                .title(dto.getTitle())
                .participationCount(dto.getParticipationCount())
                .timeslots(timeslots)
                .build();
        bookingLessonRepository.save(bookingLesson);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Successfully booked")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> cancelBooking(UUID bookingId, String email) {
        UserEntity user = UtilityHelper.findAndCheckUser(userRepository, email);

        BookingLessonEntity bookingLesson = bookingLessonRepository
                .findByIdAndUser(bookingId, user)
                .orElseThrow(() -> {
                    log.error("Booking doesn't exist");
                    return new NotFoundException("Booking doesn't exist");
                });
        bookingLessonRepository.delete(bookingLesson);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Booking successfully cancelled")
                .build());
    }
}
