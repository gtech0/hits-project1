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
@Table(name = "subject")
@NoArgsConstructor
@AllArgsConstructor
public class SubjectEntity {

    @Id
    @Column
    private UUID id;

    @Column(name = "subject_name", unique = true)
    private String subjectName;

    @OneToMany(mappedBy = "subject")
    private Set<LessonEntity> lessons;

}
