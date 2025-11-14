package org.example.timetrail.controller;

import org.example.timetrail.dto.UserDTO;
import org.example.timetrail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<UserDTO.UserProfileResponse> getProfile(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(userService.getProfile(userId));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<UserDTO.UserProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody UserDTO.UpdateProfileRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }
}