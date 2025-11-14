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
@Document(collection = "rooms")
public class Room {
    @Id
    private String roomId;

    private String roomName;

    private String description;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isActive = true;
}