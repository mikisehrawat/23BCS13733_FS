package org.example.timetrail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RoomDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoomRequest {
        private String roomName;
        private String description;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomResponse {
        private String roomId;
        private String roomName;
        private String description;
        private String createdBy;
        private LocalDateTime createdAt;
        private int memberCount;
        private boolean isAdmin;
        private List<MemberInfo> members;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private String userId;
        private String name;
        private String email;
        private boolean isAdmin;
        private LocalDateTime joinedAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRoomRequest {
        private String roomId;
    }
}