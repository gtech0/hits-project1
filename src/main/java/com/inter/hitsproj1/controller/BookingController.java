package com.inter.hitsproj1.controller;

import com.inter.hitsproj1.dto.BookingCreateDto;
import com.inter.hitsproj1.dto.BookingGetDto;
import com.inter.hitsproj1.dto.StatusResponse;
import com.inter.hitsproj1.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR', 'STUDENT')")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<BookingGetDto>> getBookings(Authentication authentication) {
        return bookingService.getBookings(authentication.getName());
    }

    @PostMapping(value = "/create", produces = "application/json")
    public ResponseEntity<StatusResponse> bookPlace(@RequestBody BookingCreateDto dto,
                                                    Authentication authentication) {
        return bookingService.bookPlace(dto, authentication.getName());
    }

    @PostMapping(value = "/{bookingId}/cancel", produces = "application/json")
    public ResponseEntity<StatusResponse> cancelBooking(@PathVariable UUID bookingId,
                                                   Authentication authentication) {
        return bookingService.cancelBooking(bookingId, authentication.getName());
    }

}
