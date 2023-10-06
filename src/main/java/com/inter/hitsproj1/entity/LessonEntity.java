package com.inter.hitsproj1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "lesson",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"date", "timeslot_id", "audience_id"}
        ))
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonEntity {

    @Id
    @Column
    private UUID id;

    @Column
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type")
    private LessonTypeEnum lessonType;

    @ManyToMany
    @JoinTable(
            name = "groups_on_lesson",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private Set<StudentGroupEntity> groups;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "audience_id")
    private AudienceEntity audience;

    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private TimeslotEntity timeslot;

}
