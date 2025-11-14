package org.example.timetrail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileRequest {
        private String name;
        private String profileImage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfileResponse {
        private String userId;
        private String name;
        private String email;
        private String profileImage;
        private LocalDateTime createdAt;
        private Long totalStudyTime;
        private int totalSessions;
        private int roomsJoined;
    }
}