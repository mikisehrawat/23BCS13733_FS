package org.example.timetrail.service;

import org.example.timetrail.dto.TagDTO;
import org.example.timetrail.exception.CustomException;
import org.example.timetrail.model.Connection;
import org.example.timetrail.model.Tag;
import org.example.timetrail.repository.ConnectionRepository;
import org.example.timetrail.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;
    
    @Autowired
    private ConnectionRepository connectionRepository;
    
    public TagDTO.TagResponse createTag(String userId, TagDTO.CreateTagRequest request) {
        // Verify user is admin of room
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, request.getRoomId())
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can create tags", HttpStatus.FORBIDDEN);
        }
        
        // Check if tag with same name already exists in room
        if (tagRepository.existsByTagNameAndRoomId(request.getTagName(), request.getRoomId())) {
            throw new CustomException("Tag with this name already exists in this room", HttpStatus.BAD_REQUEST);
        }
        
        Tag tag = new Tag();
        tag.setTagName(request.getTagName());
        tag.setTagDescription(request.getTagDescription());
        tag.setTagColor(request.getTagColor());
        tag.setRoomId(request.getRoomId());
        tag.setCreatedAt(LocalDateTime.now());
        
        Tag savedTag = tagRepository.save(tag);
        
        return convertToTagResponse(savedTag);
    }
    
    public List<TagDTO.TagResponse> getRoomTags(String roomId, String userId) {
        // Verify user is member of room
        if (!connectionRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN);
        }
        
        List<Tag> tags = tagRepository.findByRoomId(roomId);
        
        return tags.stream()
                .map(this::convertToTagResponse)
                .collect(Collectors.toList());
    }
    
    public TagDTO.TagResponse updateTag(String tagId, String userId, TagDTO.UpdateTagRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException("Tag not found", HttpStatus.NOT_FOUND));
        
        // Verify user is admin of room
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, tag.getRoomId())
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can update tags", HttpStatus.FORBIDDEN);
        }
        
        if (request.getTagName() != null) {
            // Check if new name conflicts with existing tag
            if (!tag.getTagName().equals(request.getTagName()) &&
                    tagRepository.existsByTagNameAndRoomId(request.getTagName(), tag.getRoomId())) {
                throw new CustomException("Tag with this name already exists in this room", HttpStatus.BAD_REQUEST);
            }
            tag.setTagName(request.getTagName());
        }
        
        if (request.getTagDescription() != null) {
            tag.setTagDescription(request.getTagDescription());
        }
        
        if (request.getTagColor() != null) {
            tag.setTagColor(request.getTagColor());
        }
        
        tagRepository.save(tag);
        
        return convertToTagResponse(tag);
    }
    
    public void deleteTag(String tagId, String userId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException("Tag not found", HttpStatus.NOT_FOUND));
        
        // Verify user is admin of room
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, tag.getRoomId())
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can delete tags", HttpStatus.FORBIDDEN);
        }
        
        tagRepository.delete(tag);
    }
    
    private TagDTO.TagResponse convertToTagResponse(Tag tag) {
        TagDTO.TagResponse response = new TagDTO.TagResponse();
        response.setTagId(tag.getTagId());
        response.setTagName(tag.getTagName());
        response.setTagDescription(tag.getTagDescription());
        response.setTagColor(tag.getTagColor());
        response.setRoomId(tag.getRoomId());
        response.setCreatedAt(tag.getCreatedAt());
        return response;
    }
}