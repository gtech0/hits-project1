package com.inter.hitsproj1.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {

    @Pattern(regexp="\\d{6}", message = "Group number should be 6 digits long")
    private String groupNumber;

    private Set<UUID> students;

}
