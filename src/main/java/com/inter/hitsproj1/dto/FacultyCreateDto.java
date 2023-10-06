package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyCreateDto {

    private String facultyName;

    private Set<UUID> groups;

    private Set<UUID> professors;

}
