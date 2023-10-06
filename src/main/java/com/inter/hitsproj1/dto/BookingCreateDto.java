package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {

    private UUID audienceId;

    private String date;

    private String title;

    private Integer participationCount;

    private List<TimeslotGetDto> lessons;

}
