package org.example.timetrail.service;

import org.example.timetrail.dto.SessionDTO;
import org.example.timetrail.exception.CustomException;
import org.example.timetrail.model.*;
import org.example.timetrail.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private ConnectionRepository connectionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    public SessionDTO.SessionResponse createSession(String userId, SessionDTO.CreateSessionRequest request) {
        // Verify user is member of room
        if (!connectionRepository.existsByUserIdAndRoomId(userId, request.getRoomId())) {
            throw new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN);
        }
        
        // Verify tag exists and belongs to room
        if (request.getTagId() != null) {
            Tag tag = tagRepository.findById(request.getTagId())
                    .orElseThrow(() -> new CustomException("Tag not found", HttpStatus.NOT_FOUND));
            
            if (!tag.getRoomId().equals(request.getRoomId())) {
                throw new CustomException("Tag does not belong to this room", HttpStatus.BAD_REQUEST);
            }
        }
        
        Session session = new Session();
        session.setTime(request.getTime());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setUserId(userId);
        session.setRoomId(request.getRoomId());
        session.setTagId(request.getTagId());
        session.setCreatedAt(LocalDateTime.now());
        
        Session savedSession = sessionRepository.save(session);
        
        return convertToSessionResponse(savedSession);
    }
    
    public List<SessionDTO.SessionResponse> getUserSessions(String userId) {
        List<Session> sessions = sessionRepository.findByUserId(userId);
        List<SessionDTO.SessionResponse> responses = new ArrayList<>();
        
        for (Session session : sessions) {
            responses.add(convertToSessionResponse(session));
        }
        
        return responses;
    }
    
    public List<SessionDTO.SessionResponse> getRoomSessions(String roomId, String userId) {
        // Verify user is member of room
        if (!connectionRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN);
        }
        
        List<Session> sessions = sessionRepository.findByRoomId(roomId);
        List<SessionDTO.SessionResponse> responses = new ArrayList<>();
        
        for (Session session : sessions) {
            responses.add(convertToSessionResponse(session));
        }
        
        return responses;
    }
    
    public SessionDTO.SessionResponse updateSession(String sessionId, String userId, SessionDTO.UpdateSessionRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));
        
        // Verify ownership
        if (!session.getUserId().equals(userId)) {
            throw new CustomException("You can only update your own sessions", HttpStatus.FORBIDDEN);
        }
        
        if (request.getTime() != null) {
            session.setTime(request.getTime());
        }
        
        if (request.getTagId() != null) {
            Tag tag = tagRepository.findById(request.getTagId())
                    .orElseThrow(() -> new CustomException("Tag not found", HttpStatus.NOT_FOUND));
            
            if (!tag.getRoomId().equals(session.getRoomId())) {
                throw new CustomException("Tag does not belong to this room", HttpStatus.BAD_REQUEST);
            }
            
            session.setTagId(request.getTagId());
        }
        
        sessionRepository.save(session);
        
        return convertToSessionResponse(session);
    }
    
    public void deleteSession(String sessionId, String userId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException("Session not found", HttpStatus.NOT_FOUND));
        
        // Verify ownership
        if (!session.getUserId().equals(userId)) {
            throw new CustomException("You can only delete your own sessions", HttpStatus.FORBIDDEN);
        }
        
        sessionRepository.delete(session);
    }
    
    private SessionDTO.SessionResponse convertToSessionResponse(Session session) {
        User user = userRepository.findById(session.getUserId()).orElse(null);
        Room room = roomRepository.findById(session.getRoomId()).orElse(null);
        Tag tag = session.getTagId() != null ? tagRepository.findById(session.getTagId()).orElse(null) : null;
        
        SessionDTO.SessionResponse response = new SessionDTO.SessionResponse();
        response.setSessionId(session.getSessionId());
        response.setTime(session.getTime());
        response.setStartTime(session.getStartTime());
        response.setEndTime(session.getEndTime());
        response.setUserId(session.getUserId());
        response.setUserName(user != null ? user.getName() : "Unknown");
        response.setRoomId(session.getRoomId());
        response.setRoomName(room != null ? room.getRoomName() : "Unknown");
        response.setTagId(session.getTagId());
        response.setTagName(tag != null ? tag.getTagName() : null);
        response.setTagColor(tag != null ? tag.getTagColor() : null);
        response.setCreatedAt(session.getCreatedAt());
        
        return response;
    }
}