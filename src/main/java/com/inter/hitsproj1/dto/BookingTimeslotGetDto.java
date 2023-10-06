package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingTimeslotGetDto {

    private UUID id;

    private Integer lessonNumber;

    private LocalTime start;

    private LocalTime end;

}
