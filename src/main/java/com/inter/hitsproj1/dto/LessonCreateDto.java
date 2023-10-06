package com.inter.hitsproj1.dto;

import com.inter.hitsproj1.entity.LessonTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateDto {

    private String date;

    @Enumerated(EnumType.STRING)
    private LessonTypeEnum lessonType;

    private Set<UUID> groups;

    private UUID subject;

    private UUID professor;

    private UUID audience;

    @Min(value = 1)
    @Max(value = 7)
    private Integer timeslotNumber;

}
