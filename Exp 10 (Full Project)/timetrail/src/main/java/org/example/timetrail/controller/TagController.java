package org.example.timetrail.controller;

import org.example.timetrail.dto.TagDTO;
import org.example.timetrail.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    
    @Autowired
    private TagService tagService;
    
    @PostMapping
    public ResponseEntity<TagDTO.TagResponse> createTag(
            Authentication authentication,
            @RequestBody TagDTO.CreateTagRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(tagService.createTag(userId, request));
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<TagDTO.TagResponse>> getRoomTags(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(tagService.getRoomTags(roomId, userId));
    }
    
    @PutMapping("/{tagId}")
    public ResponseEntity<TagDTO.TagResponse> updateTag(
            @PathVariable String tagId,
            Authentication authentication,
            @RequestBody TagDTO.UpdateTagRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(tagService.updateTag(tagId, userId, request));
    }
    
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable String tagId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        tagService.deleteTag(tagId, userId);
        return ResponseEntity.ok().build();
    }
}