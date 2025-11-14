package org.example.timetrail.service;

import org.example.timetrail.dto.RoomDTO;
import org.example.timetrail.exception.CustomException;
import org.example.timetrail.model.Connection;
import org.example.timetrail.model.JoinRequest;
import org.example.timetrail.model.Room;
import org.example.timetrail.model.User;
import org.example.timetrail.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private ConnectionRepository connectionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JoinRequestRepository joinRequestRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private TagRepository tagRepository;
    
    @Transactional
    public RoomDTO.RoomResponse createRoom(String userId, RoomDTO.CreateRoomRequest request) {
        // Create room
        Room room = new Room();
        room.setRoomName(request.getRoomName());
        room.setDescription(request.getDescription());
        room.setCreatedBy(userId);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());
        room.setActive(true);
        
        Room savedRoom = roomRepository.save(room);
        
        // Add creator as admin
        Connection connection = new Connection();
        connection.setUserId(userId);
        connection.setRoomId(savedRoom.getRoomId());
        connection.setAdmin(true);
        connection.setJoinedAt(LocalDateTime.now());
        connectionRepository.save(connection);
        
        return getRoomDetails(savedRoom.getRoomId(), userId);
    }
    
    public RoomDTO.RoomResponse getRoomDetails(String roomId, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Room not found", HttpStatus.NOT_FOUND));
        
        // Check if user is member
        Connection userConnection = connectionRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        // Get all members
        List<Connection> connections = connectionRepository.findByRoomId(roomId);
        List<RoomDTO.MemberInfo> members = new ArrayList<>();
        
        for (Connection conn : connections) {
            User user = userRepository.findById(conn.getUserId()).orElse(null);
            if (user != null) {
                RoomDTO.MemberInfo memberInfo = new RoomDTO.MemberInfo();
                memberInfo.setUserId(user.getUserId());
                memberInfo.setName(user.getName());
                memberInfo.setEmail(user.getEmail());
                memberInfo.setAdmin(conn.isAdmin());
                memberInfo.setJoinedAt(conn.getJoinedAt());
                members.add(memberInfo);
            }
        }
        
        RoomDTO.RoomResponse response = new RoomDTO.RoomResponse();
        response.setRoomId(room.getRoomId());
        response.setRoomName(room.getRoomName());
        response.setDescription(room.getDescription());
        response.setCreatedBy(room.getCreatedBy());
        response.setCreatedAt(room.getCreatedAt());
        response.setMemberCount(connections.size());
        response.setAdmin(userConnection.isAdmin());
        response.setMembers(members);
        
        return response;
    }
    
    public void requestToJoinRoom(String userId, String roomId) {
        // Check if room exists
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Room not found", HttpStatus.NOT_FOUND));
        
        // Check if already a member
        if (connectionRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new CustomException("You are already a member of this room", HttpStatus.BAD_REQUEST);
        }
        
        // Check if request already exists
        if (joinRequestRepository.existsByUserIdAndRoomIdAndStatus(userId, roomId, "PENDING")) {
            throw new CustomException("You have already requested to join this room", HttpStatus.BAD_REQUEST);
        }
        
        // Create join request
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setUserId(userId);
        joinRequest.setRoomId(roomId);
        joinRequest.setStatus("PENDING");
        joinRequest.setRequestedAt(LocalDateTime.now());
        
        joinRequestRepository.save(joinRequest);
    }
    
    public List<JoinRequest> getRoomRequests(String roomId, String userId) {
        // Check if user is admin
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can view join requests", HttpStatus.FORBIDDEN);
        }
        
        return joinRequestRepository.findByRoomIdAndStatus(roomId, "PENDING");
    }
    
    @Transactional
    public void acceptJoinRequest(String requestId, String userId) {
        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException("Request not found", HttpStatus.NOT_FOUND));
        
        // Check if user is admin
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, request.getRoomId())
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can accept requests", HttpStatus.FORBIDDEN);
        }
        
        // Update request status
        request.setStatus("ACCEPTED");
        request.setRespondedAt(LocalDateTime.now());
        joinRequestRepository.save(request);
        
        // Add user to room
        Connection newConnection = new Connection();
        newConnection.setUserId(request.getUserId());
        newConnection.setRoomId(request.getRoomId());
        newConnection.setAdmin(false);
        newConnection.setJoinedAt(LocalDateTime.now());
        connectionRepository.save(newConnection);
    }
    
    public void rejectJoinRequest(String requestId, String userId) {
        JoinRequest request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException("Request not found", HttpStatus.NOT_FOUND));
        
        // Check if user is admin
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, request.getRoomId())
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can reject requests", HttpStatus.FORBIDDEN);
        }
        
        // Update request status
        request.setStatus("REJECTED");
        request.setRespondedAt(LocalDateTime.now());
        joinRequestRepository.save(request);
    }
    
    @Transactional
    public void leaveRoom(String roomId, String userId) {
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.NOT_FOUND));
        
        // Check if user is the only admin
        if (connection.isAdmin()) {
            long adminCount = connectionRepository.findByRoomId(roomId).stream()
                    .filter(Connection::isAdmin)
                    .count();
            
            if (adminCount == 1) {
                long memberCount = connectionRepository.countByRoomId(roomId);
                if (memberCount > 1) {
                    throw new CustomException("Cannot leave room as the only admin. Assign another admin first.", HttpStatus.BAD_REQUEST);
                }
            }
        }
        
        connectionRepository.deleteByUserIdAndRoomId(userId, roomId);
    }
    
    @Transactional
    public void removeMember(String roomId, String memberId, String adminId) {
        // Check if admin
        Connection adminConnection = connectionRepository.findByUserIdAndRoomId(adminId, roomId)
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!adminConnection.isAdmin()) {
            throw new CustomException("Only admins can remove members", HttpStatus.FORBIDDEN);
        }
        
        // Cannot remove yourself
        if (adminId.equals(memberId)) {
            throw new CustomException("Cannot remove yourself. Use leave room instead.", HttpStatus.BAD_REQUEST);
        }
        
        Connection memberConnection = connectionRepository.findByUserIdAndRoomId(memberId, roomId)
                .orElseThrow(() -> new CustomException("Member not found in this room", HttpStatus.NOT_FOUND));
        
        connectionRepository.deleteByUserIdAndRoomId(memberId, roomId);
    }
    
    @Transactional
    public void deleteRoom(String roomId, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Room not found", HttpStatus.NOT_FOUND));
        
        // Check if user is admin
        Connection connection = connectionRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN));
        
        if (!connection.isAdmin()) {
            throw new CustomException("Only admins can delete rooms", HttpStatus.FORBIDDEN);
        }
        
        // Delete all connections
        List<Connection> connections = connectionRepository.findByRoomId(roomId);
        connectionRepository.deleteAll(connections);
        
        // Delete all join requests
        List<JoinRequest> requests = joinRequestRepository.findByRoomId(roomId);
        joinRequestRepository.deleteAll(requests);
        
        // Delete all tags
        tagRepository.deleteAll(tagRepository.findByRoomId(roomId));
        
        // Delete all sessions (optional - you might want to keep for history)
        // sessionRepository.deleteAll(sessionRepository.findByRoomId(roomId));
        
        // Delete room
        roomRepository.delete(room);
    }
    
    public List<RoomDTO.RoomResponse> getUserRooms(String userId) {
        List<Connection> connections = connectionRepository.findByUserId(userId);
        
        return connections.stream()
                .map(conn -> getRoomDetails(conn.getRoomId(), userId))
                .collect(Collectors.toList());
    }
}