package com.inter.hitsproj1.repository;

import com.inter.hitsproj1.entity.TimeslotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TimeslotRepository extends JpaRepository<TimeslotEntity, UUID> {
    TimeslotEntity findByTimeslotNumber(Integer number);
    Optional<TimeslotEntity> findByTimeslotNumberAndStartDateAndEndDate(Integer timeslotNumber,
                                                                        LocalTime startDate,
                                                                        LocalTime endDate);
}
