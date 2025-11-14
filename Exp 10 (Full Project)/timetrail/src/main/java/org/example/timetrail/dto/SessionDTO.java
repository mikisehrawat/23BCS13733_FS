package org.example.timetrail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SessionDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateSessionRequest {
        private Long time; // in seconds
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String roomId;
        private String tagId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionResponse {
        private String sessionId;
        private Long time;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String userId;
        private String userName;
        private String roomId;
        private String roomName;
        private String tagId;
        private String tagName;
        private String tagColor;
        private LocalDateTime createdAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSessionRequest {
        private Long time;
        private String tagId;
    }
}