package org.example.timetrail.service;

import org.example.timetrail.dto.UserDTO;
import org.example.timetrail.exception.CustomException;
import org.example.timetrail.model.Connection;
import org.example.timetrail.model.Session;
import org.example.timetrail.model.User;
import org.example.timetrail.repository.ConnectionRepository;
import org.example.timetrail.repository.SessionRepository;
import org.example.timetrail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private ConnectionRepository connectionRepository;
    
    public UserDTO.UserProfileResponse getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        
        List<Session> sessions = sessionRepository.findByUserId(userId);
        Long totalStudyTime = sessions.stream()
                .mapToLong(Session::getTime)
                .sum();
        
        List<Connection> connections = connectionRepository.findByUserId(userId);
        
        UserDTO.UserProfileResponse response = new UserDTO.UserProfileResponse();
        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setProfileImage(user.getProfileImage());
        response.setCreatedAt(user.getCreatedAt());
        response.setTotalStudyTime(totalStudyTime);
        response.setTotalSessions(sessions.size());
        response.setRoomsJoined(connections.size());
        
        return response;
    }
    
    public UserDTO.UserProfileResponse updateProfile(String userId, UserDTO.UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getProfileImage() != null) {
            user.setProfileImage(request.getProfileImage());
        }
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
        
        return getProfile(userId);
    }
}