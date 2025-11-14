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
@Document(collection = "tags")
public class Tag {
    @Id
    private String tagId;
    
    private String tagName;
    
    private String tagDescription;
    
    private String tagColor;
    
    private String roomId;
    
    private LocalDateTime createdAt;
}