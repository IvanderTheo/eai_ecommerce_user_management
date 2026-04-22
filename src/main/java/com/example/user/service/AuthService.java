package com.example.user.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import com.example.user.security.JwtUtils;
import com.example.user.dto.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse register(String username, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("USER");
            userRepository.save(user);

            // Generate token di sini supaya langsung kelihatan hasilnya
            String token = jwtUtils.generateToken(username); 
            return new AuthResponse("User registered successfully", token, true);
        } catch (Exception e) {
            return new AuthResponse("Registration failed: " + e.getMessage(), null, false);
        }
    }

    public AuthResponse login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Wrong password");
            }

            String token = jwtUtils.generateToken(username);
            return new AuthResponse("Login successful", token, true);
        } catch (Exception e) {
            return new AuthResponse("Login failed: " + e.getMessage(), null, false);
        }
    }
}