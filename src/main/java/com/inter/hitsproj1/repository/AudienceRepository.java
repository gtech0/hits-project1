package com.inter.hitsproj1.repository;

import com.inter.hitsproj1.entity.AudienceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AudienceRepository extends JpaRepository<AudienceEntity, UUID> {
}
