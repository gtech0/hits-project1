package com.inter.hitsproj1.repository;

import com.inter.hitsproj1.entity.AudienceEntity;
import com.inter.hitsproj1.entity.BookingLessonEntity;
import com.inter.hitsproj1.entity.TimeslotEntity;
import com.inter.hitsproj1.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingLessonRepository extends JpaRepository<BookingLessonEntity, UUID> {
    Optional<BookingLessonEntity> findByIdAndUser(UUID id, UserEntity user);
    List<BookingLessonEntity> findAllByUser(UserEntity user);
    @Query("SELECT bl FROM BookingLessonEntity bl " +
            "WHERE :timeslot MEMBER OF bl.timeslots " +
            "AND bl.audience = :auditorium " +
            "AND bl.date = :date")
    List<BookingLessonEntity> findByTimeslotsAndAudienceAndDate(TimeslotEntity timeslot,
                                                                AudienceEntity auditorium,
                                                                LocalDate date);
}
