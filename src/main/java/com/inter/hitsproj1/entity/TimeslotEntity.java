package com.inter.hitsproj1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "timeslot")
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotEntity {

    @Id
    @Column
    private UUID id;

    @Column(name = "timeslot_number")
    private Integer timeslotNumber;

    @Column(name = "start_date")
    private LocalTime startDate;

    @Column(name = "end_date")
    private LocalTime endDate;

    @OneToMany(mappedBy = "timeslot")
    private Set<LessonEntity> lessons;

    @ManyToMany(mappedBy = "timeslots")
    private Set<BookingLessonEntity> bookingLessons;

}
