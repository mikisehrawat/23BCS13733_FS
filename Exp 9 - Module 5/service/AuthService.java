package org.example.timetrail.service;

import org.example.timetrail.dto.AuthDTO;
import org.example.timetrail.exception.CustomException;
import org.example.timetrail.model.User;
import org.example.timetrail.repository.UserRepository;
import org.example.timetrail.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthDTO.AuthResponse signup(AuthDTO.SignupRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already registered", HttpStatus.BAD_REQUEST);
        }
        
        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getUserId(), savedUser.getEmail());
        
        return new AuthDTO.AuthResponse(
                token,
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
    
    public AuthDTO.AuthResponse login(AuthDTO.LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid email or password", HttpStatus.UNAUTHORIZED));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
        
        return new AuthDTO.AuthResponse(
                token,
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}