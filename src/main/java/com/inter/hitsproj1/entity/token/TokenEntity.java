package com.inter.hitsproj1.entity.token;

import com.inter.hitsproj1.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "token")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenEntity {

    @Id
    @Column
    private UUID id;

    @Column
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    private TokenType tokenType;

    @Column
    private boolean expired;

    @Column
    private  boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
