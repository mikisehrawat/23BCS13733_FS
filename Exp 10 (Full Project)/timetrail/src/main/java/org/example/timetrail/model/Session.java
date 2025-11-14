package org.example.timetrail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sessions")
public class Session {
    @Id
    private String sessionId;
    
    private Long time; // Time in seconds
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String userId;
    
    private String roomId;
    
    private String tagId;
    
    private LocalDateTime createdAt;
}