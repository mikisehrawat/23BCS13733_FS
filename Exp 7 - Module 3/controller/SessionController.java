package org.example.timetrail.controller;

import org.example.timetrail.dto.SessionDTO;
import org.example.timetrail.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    
    @Autowired
    private SessionService sessionService;
    
    @PostMapping
    public ResponseEntity<SessionDTO.SessionResponse> createSession(
            Authentication authentication,
            @RequestBody SessionDTO.CreateSessionRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(sessionService.createSession(userId, request));
    }
    
    @GetMapping
    public ResponseEntity<List<SessionDTO.SessionResponse>> getUserSessions(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(sessionService.getUserSessions(userId));
    }
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<SessionDTO.SessionResponse>> getRoomSessions(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(sessionService.getRoomSessions(roomId, userId));
    }
    
    @PutMapping("/{sessionId}")
    public ResponseEntity<SessionDTO.SessionResponse> updateSession(
            @PathVariable String sessionId,
            Authentication authentication,
            @RequestBody SessionDTO.UpdateSessionRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(sessionService.updateSession(sessionId, userId, request));
    }
    
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(
            @PathVariable String sessionId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        sessionService.deleteSession(sessionId, userId);
        return ResponseEntity.ok().build();
    }
}