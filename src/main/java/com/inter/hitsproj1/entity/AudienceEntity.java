package com.inter.hitsproj1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "audience")
@NoArgsConstructor
@AllArgsConstructor
public class AudienceEntity {

    @Id
    @Column
    private UUID id;

    @Column(name = "audience_name", unique = true)
    private String audienceName;

    @OneToMany(mappedBy = "audience")
    private Set<LessonEntity> lessons;

    @OneToMany(mappedBy = "audience")
    private Set<BookingLessonEntity> bookingLessons;

}
