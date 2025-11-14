package org.example.timetrail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TagDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateTagRequest {
        private String tagName;
        private String tagDescription;
        private String tagColor;
        private String roomId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagResponse {
        private String tagId;
        private String tagName;
        private String tagDescription;
        private String tagColor;
        private String roomId;
        private LocalDateTime createdAt;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateTagRequest {
        private String tagName;
        private String tagDescription;
        private String tagColor;
    }
}