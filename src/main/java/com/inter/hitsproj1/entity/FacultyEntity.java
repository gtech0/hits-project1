package com.inter.hitsproj1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "faculty")
@NoArgsConstructor
@AllArgsConstructor
public class FacultyEntity {

    @Id
    @Column
    private UUID id;

    @Column(name = "faculty_name", unique = true)
    private String facultyName;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Set<StudentGroupEntity> groups;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private Set<UserEntity> users;

}
