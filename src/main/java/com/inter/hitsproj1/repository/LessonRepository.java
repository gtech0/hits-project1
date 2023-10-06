package com.inter.hitsproj1.repository;

import com.inter.hitsproj1.entity.AudienceEntity;
import com.inter.hitsproj1.entity.LessonEntity;
import com.inter.hitsproj1.entity.TimeslotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, UUID> {
    @Query("SELECT l " +
            "FROM LessonEntity l " +
            "WHERE l.date = :date " +
            "ORDER BY l.timeslot.timeslotNumber")
    List<LessonEntity> findLessonsByDate(LocalDate date);

    List<LessonEntity> findAllByAudienceAndDateAndTimeslot(AudienceEntity auditorium,
                                                           LocalDate date,
                                                           TimeslotEntity timeslot);
}
