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
@Document(collection = "join_requests")
public class JoinRequest {
    @Id
    private String id;
    
    private String userId;
    
    private String roomId;
    
    private String status; // PENDING, ACCEPTED, REJECTED
    
    private LocalDateTime requestedAt;
    
    private LocalDateTime respondedAt;
}