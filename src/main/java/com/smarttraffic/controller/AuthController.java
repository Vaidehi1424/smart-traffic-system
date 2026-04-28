package com.smarttraffic.controller;

import com.smarttraffic.dto.ApiResponse;
import com.smarttraffic.model.Role;
import com.smarttraffic.model.User;
import com.smarttraffic.repository.UserRepository;
import com.smarttraffic.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody Map<String, String> body) {

        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "Username and password required", null));
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            String role = auth.getAuthorities().iterator().next()
                    .getAuthority().replace("ROLE_", "");

            String token = jwtUtil.generateToken(username, role);

            return ResponseEntity.ok(new ApiResponse<>(200, "Login successful",
                    Map.of("token", token, "role", role, "username", username)));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Invalid credentials", null));
        }
    }

    // ✅ REGISTER
  @PostMapping("/register")
public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) {

    if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null ||
        user.getUsername().isBlank() || user.getPassword().isBlank() || user.getEmail().isBlank()) {

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(400, "Username, email and password required", null));
    }

    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(400, "Username already exists", null));
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(Role.USER);

    userRepository.save(user);

    return ResponseEntity.ok(new ApiResponse<>(200, "Registered successfully", null));
}
}



