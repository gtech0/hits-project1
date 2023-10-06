package com.inter.hitsproj1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "booking_lesson")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingLessonEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "audience_id")
    private AudienceEntity audience;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private LocalDate date;

    @Column
    private String title;

    @Column(name = "participation_count")
    private Integer participationCount;

    @ManyToMany
    @JoinTable(
            name = "booked_timeslots",
            joinColumns = @JoinColumn(name = "booking_lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "timeslot_id"))
    private Set<TimeslotEntity> timeslots;

}
