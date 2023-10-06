package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeslotGetDto {

    private Integer lessonNumber;

    private String start;

    private String end;

}
