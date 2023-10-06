package com.inter.hitsproj1.dto;

import com.inter.hitsproj1.entity.RoleEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private String email;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Set<RoleEnum> roles;

}
