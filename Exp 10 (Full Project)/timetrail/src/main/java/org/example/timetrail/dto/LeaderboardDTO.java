package org.example.timetrail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class LeaderboardDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaderboardEntry {
        private int rank;
        private String userId;
        private String userName;
        private Long totalTime;
        private int sessionCount;
        private String profileImage;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaderboardResponse {
        private String type; // ROOM, TAG, GLOBAL
        private String roomId;
        private String roomName;
        private String tagId;
        private String tagName;
        private List<LeaderboardEntry> entries;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagLeaderboardEntry {
        private String tagId;
        private String tagName;
        private String tagColor;
        private Long totalTime;
        private int sessionCount;
    }
}