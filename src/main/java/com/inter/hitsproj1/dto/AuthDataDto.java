package com.inter.hitsproj1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDataDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
