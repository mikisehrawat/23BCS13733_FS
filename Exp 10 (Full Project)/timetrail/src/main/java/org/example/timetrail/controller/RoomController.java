package org.example.timetrail.controller;

import org.example.timetrail.dto.RoomDTO;
import org.example.timetrail.model.JoinRequest;
import org.example.timetrail.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @PostMapping
    public ResponseEntity<RoomDTO.RoomResponse> createRoom(
            Authentication authentication,
            @RequestBody RoomDTO.CreateRoomRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(roomService.createRoom(userId, request));
    }
    
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO.RoomResponse> getRoomDetails(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(roomService.getRoomDetails(roomId, userId));
    }
    
    @GetMapping
    public ResponseEntity<List<RoomDTO.RoomResponse>> getUserRooms(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(roomService.getUserRooms(userId));
    }
    
    @PostMapping("/join")
    public ResponseEntity<Void> requestToJoinRoom(
            Authentication authentication,
            @RequestBody RoomDTO.JoinRoomRequest request) {
        String userId = authentication.getPrincipal().toString();
        roomService.requestToJoinRoom(userId, request.getRoomId());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{roomId}/requests")
    public ResponseEntity<List<JoinRequest>> getRoomRequests(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(roomService.getRoomRequests(roomId, userId));
    }
    
    @PostMapping("/requests/{requestId}/accept")
    public ResponseEntity<Void> acceptJoinRequest(
            @PathVariable String requestId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        roomService.acceptJoinRequest(requestId, userId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/requests/{requestId}/reject")
    public ResponseEntity<Void> rejectJoinRequest(
            @PathVariable String requestId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        roomService.rejectJoinRequest(requestId, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        roomService.leaveRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable String roomId,
            @PathVariable String memberId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        roomService.removeMember(roomId, memberId, userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        roomService.deleteRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }
}