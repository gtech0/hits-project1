package com.inter.hitsproj1.repository;

import com.inter.hitsproj1.entity.token.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {
    @Query(value =
            "SELECT t " +
            "FROM TokenEntity t " +
            "WHERE t.user.id = :userId " +
            "AND t.expired = false " +
            "AND t.revoked = false")
    List<TokenEntity> findAllValidTokensByUser(UUID userId);
    Optional<TokenEntity> findByToken(String token);
}
