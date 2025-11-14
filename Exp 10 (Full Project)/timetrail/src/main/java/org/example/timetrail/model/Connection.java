package org.example.timetrail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "connections")
@CompoundIndex(name = "user_room_idx", def = "{'userId': 1, 'roomId': 1}", unique = true)
public class Connection {
    @Id
    private String connectionId;
    
    private String userId;
    
    private String roomId;
    
    private boolean isAdmin;
    
    private LocalDateTime joinedAt;
}