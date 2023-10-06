package com.inter.hitsproj1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inter.hitsproj1.dto.*;
import com.inter.hitsproj1.entity.RoleEnum;
import com.inter.hitsproj1.entity.StudentGroupEntity;
import com.inter.hitsproj1.entity.UserEntity;
import com.inter.hitsproj1.entity.token.TokenEntity;
import com.inter.hitsproj1.entity.token.TokenType;
import com.inter.hitsproj1.exception.NotFoundException;
import com.inter.hitsproj1.exception.PermissionDeniedException;
import com.inter.hitsproj1.exception.UniqueConstraintViolationException;
import com.inter.hitsproj1.repository.StudentGroupRepository;
import com.inter.hitsproj1.repository.TokenRepository;
import com.inter.hitsproj1.repository.UserRepository;
import com.inter.hitsproj1.util.UtilityHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentGroupRepository groupRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<JwtTokenDto> registration(RegistrationDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            log.error("User " + dto.getEmail() + " already exists");
            throw new UniqueConstraintViolationException("User " + dto.getEmail() + " already exists");
        }

        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(new HashSet<>())
                .build();
        UserEntity savedUser = userRepository.save(user);

        String accessToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, accessToken);

        return ResponseEntity.ok(new JwtTokenDto(accessToken, refreshToken));
    }

    @Transactional
    public ResponseEntity<JwtTokenDto> authorization(AuthDataDto dto) {
        UserEntity user = UtilityHelper.findAndCheckUser(userRepository, dto.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(),
                        dto.getPassword()
                )
        );

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);

        return ResponseEntity.ok(new JwtTokenDto(accessToken, refreshToken));
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        tokenRepository.save(TokenEntity.builder()
                .id(UUID.randomUUID())
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build());
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Transactional
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        email = jwtService.extractUsername(refreshToken);
        if (email != null) {
            UserEntity user = this.userRepository.findByEmail(email)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                JwtTokenDto authResponse = JwtTokenDto.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public ResponseEntity<UserInfoDto> userProfileInfo(String email) {
        UserEntity user = UtilityHelper.findAndCheckUser(userRepository, email);

        return ResponseEntity.ok(new UserInfoDto(
                user.getEmail(),
                user.getFullName(),
                user.getRoles()
        ));
    }

    public ResponseEntity<UserInfoDto> selfProfileInfo(String login) {
        return userProfileInfo(login);
    }

    @Transactional
    public ResponseEntity<StatusResponse> changeUserProfile(ProfileChangeDto dto,
                                                            UUID userId,
                                                            String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> {
                    log.error("User doesn't exist");
                    return new NotFoundException("User doesn't exist");
                });

        if (user.getRoles().contains(RoleEnum.ADMIN)) {
            throw new PermissionDeniedException("Can't change profile of another admin");
        }

        user.setFullName(dto.getFullName());
        user.setRoles(dto.getRoles());
        userRepository.save(user);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Profile changed")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> changeAdminProfile(AdminChangeDto dto,
                                                             String email) {
        UserEntity admin = UtilityHelper.findAndCheckUser(userRepository, email);

        if (!admin.getRoles().contains(RoleEnum.ADMIN)) {
            log.info("Access denied");
            throw new PermissionDeniedException("Access denied");
        }

        admin.setFullName(dto.getFullName());
        userRepository.save(admin);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Profile changed")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> createGroup(GroupDto dto, String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        StudentGroupEntity group = StudentGroupEntity.builder()
                .id(UUID.randomUUID())
                .groupNumber(dto.getGroupNumber())
                .build();

        Set<UserEntity> students = new HashSet<>();
        for (UUID student : dto.getStudents()) {
            UserEntity studentUser = UtilityHelper
                    .checkRole(student, userRepository, RoleEnum.STUDENT);
            if (studentUser.getGroup() != null) {
                log.error("User is already in group");
                throw new UniqueConstraintViolationException("User is already in group");
            }
            studentUser.setGroup(group);
            students.add(studentUser);
        }

        group.setUsers(students);
        groupRepository.save(group);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Group created")
                .build());
    }

    @Transactional
    public ResponseEntity<StatusResponse> changeGroup(GroupDto dto,
                                                      UUID groupId,
                                                      String email) {
        UtilityHelper.findAndCheckUser(userRepository, email);

        StudentGroupEntity group = UtilityHelper.findAndCheckGroup(groupRepository, groupId);

        group.getUsers()
                .stream()
                .filter(student -> !dto.getStudents().contains(student.getId()))
                .forEach(student -> {
                    student.setGroup(null);
                    userRepository.save(student);
                });

        Set<UserEntity> students = dto.getStudents()
                .stream()
                .map(studentId -> {
                    UserEntity studentUser = UtilityHelper
                            .checkRole(studentId, userRepository, RoleEnum.STUDENT);
                    studentUser.setGroup(group);
                    return studentUser;
                })
                .collect(Collectors.toSet());

        group.setGroupNumber(dto.getGroupNumber());
        group.setUsers(students);
        groupRepository.save(group);

        return ResponseEntity.ok(StatusResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.OK.value())
                .message("Group updated")
                .build());
    }
}
