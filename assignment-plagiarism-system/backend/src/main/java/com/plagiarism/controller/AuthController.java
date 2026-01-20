package com.plagiarism.controller;

import com.plagiarism.dto.AuthResponse;
import com.plagiarism.dto.LoginRequest;
import com.plagiarism.dto.RegisterRequest;
import com.plagiarism.entity.User;
import com.plagiarism.entity.UserRole;
import com.plagiarism.security.JwtTokenProvider;
import com.plagiarism.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (request.getEmail() == null || request.getPassword() == null || request.getFullName() == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }

            UserRole role = UserRole.STUDENT; // Default role
            if (request.getRole() != null) {
                try {
                    role = UserRole.valueOf(request.getRole().toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body("Invalid role");
                }
            }

            User user = userService.createUser(
                    request.getEmail(),
                    request.getPassword(),
                    request.getFullName(),
                    role
            );

            String token = jwtTokenProvider.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token, "Bearer", userService.convertToDTO(user)));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(token, "Bearer", userService.convertToDTO(user)));

        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        try {
            String email = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getName();
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(userService.convertToDTO(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
    }
}
