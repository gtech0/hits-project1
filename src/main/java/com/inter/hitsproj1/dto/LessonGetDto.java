package com.inter.hitsproj1.dto;

import com.inter.hitsproj1.entity.LessonTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonGetDto {

    private UUID id;

    private Integer lessonNumber;

    private LocalTime starts;

    private LocalTime ends;

    private LessonTypeEnum lessonType;

    private Set<GroupGetDto> groups;

    private SubjectGetDto subject;

    private ProfessorGetDto professor;

    private AudienceGetDto audience;

}
