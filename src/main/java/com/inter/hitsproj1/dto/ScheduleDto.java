package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

    private LocalDate date;

    private List<LessonGetDto> lessons;

    private Integer lessonNumber;

    private LocalTime starts;

    private LocalTime ends;

}
