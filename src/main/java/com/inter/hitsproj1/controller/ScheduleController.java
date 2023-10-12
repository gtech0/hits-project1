package com.inter.hitsproj1.controller;

import com.inter.hitsproj1.dto.LessonCreateDto;
import com.inter.hitsproj1.dto.ScheduleDto;
import com.inter.hitsproj1.dto.StatusResponse;
import com.inter.hitsproj1.entity.ScheduleTypeEnum;
import com.inter.hitsproj1.service.ScheduleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('SCHEDULER', 'ADMIN')")
    @PostMapping(value = "/create/lesson", produces = "application/json")
    public ResponseEntity<StatusResponse> createLesson(@RequestBody @Valid LessonCreateDto dto,
                                                       Authentication authentication) {
        return scheduleService.createLesson(dto, authentication.getName());
    }

    @GetMapping(value = "/{type}", produces = "application/json")
    public ResponseEntity<List<ScheduleDto>> getSchedule(@PathVariable ScheduleTypeEnum type,
                                                         @RequestParam UUID typeId,
                                                         @RequestParam String dateFrom,
                                                         @RequestParam String dateTo) {
        return scheduleService.getSchedule(type, typeId, dateFrom, dateTo);
    }

    @GetMapping(value = "/week", produces = "application/json")
    public ResponseEntity<List<ScheduleDto>> getCombinedSchedule(@RequestParam(value = "group", required = false)
                                                                 Set<UUID> groupIds,
                                                                 @RequestParam(value = "audience", required = false)
                                                                 Set<UUID> audienceIds,
                                                                 @RequestParam(value = "professor", required = false)
                                                                 Set<UUID> professorIds,
                                                                 @RequestParam String dateFrom,
                                                                 @RequestParam String dateTo) {
        return scheduleService.getCombinedSchedule(groupIds, audienceIds, professorIds, dateFrom, dateTo);
    }
}
