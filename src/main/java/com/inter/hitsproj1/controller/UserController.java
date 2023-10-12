package com.inter.hitsproj1.controller;

import com.inter.hitsproj1.dto.AuthDataDto;
import com.inter.hitsproj1.dto.JwtTokenDto;
import com.inter.hitsproj1.dto.RegistrationDto;
import com.inter.hitsproj1.dto.UserInfoDto;
import com.inter.hitsproj1.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/registration", produces = "application/json")
    public ResponseEntity<JwtTokenDto> registration(@Valid @RequestBody RegistrationDto dto) {
        return userService.registration(dto);
    }

    @PostMapping(value = "/auth", produces = "application/json")
    public ResponseEntity<JwtTokenDto> auth(@Valid @RequestBody AuthDataDto authRequest) {
        return userService.authorization(authRequest);
    }

    @PostMapping(value = "/refresh-token", produces = "application/json")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        userService.refreshToken(request, response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/profile", produces = "application/json")
    public ResponseEntity<UserInfoDto> selfProfileInfo(Authentication authentication) {
        return userService.selfProfileInfo(authentication.getName());
    }

    @GetMapping(value = "/profile/{username}", produces = "application/json")
    public ResponseEntity<UserInfoDto> userProfileInfo(@PathVariable String username) {
        return userService.userProfileInfo(username);
    }
}
