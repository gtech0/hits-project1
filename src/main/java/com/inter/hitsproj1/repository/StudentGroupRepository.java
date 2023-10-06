package com.inter.hitsproj1.repository;

import com.inter.hitsproj1.entity.StudentGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroupEntity, UUID> {
    Set<StudentGroupEntity> findAllByFaculty_Id(UUID facultyId);
}
