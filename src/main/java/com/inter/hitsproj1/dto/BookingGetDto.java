package com.inter.hitsproj1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingGetDto {

    private UUID id;

    private String title;

    private Integer participationCount;

    private LocalDate date;

    private AudienceGetDto audience;

    private BookingUserGetDto user;

    private List<BookingTimeslotGetDto> lessons;

}
