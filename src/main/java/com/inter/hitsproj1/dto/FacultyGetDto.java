package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyGetDto {

    private UUID id;

    private String name;

}
