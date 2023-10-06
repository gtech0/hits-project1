package com.inter.hitsproj1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "student_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupEntity {

    @Id
    @Column
    private UUID id;

    @Column(name = "group_number", unique = true)
    private String groupNumber;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyEntity faculty;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private Set<UserEntity> users;

    @ManyToMany(mappedBy = "groups")
    private Set<LessonEntity> lessons;
}
