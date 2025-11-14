package org.example.timetrail.service;

import org.example.timetrail.dto.LeaderboardDTO;
import org.example.timetrail.exception.CustomException;
import org.example.timetrail.model.*;
import org.example.timetrail.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    
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
    
    public LeaderboardDTO.LeaderboardResponse getRoomLeaderboard(String roomId, String userId) {
        // Verify user is member of room
        if (!connectionRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN);
        }
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Room not found", HttpStatus.NOT_FOUND));
        
        List<Session> sessions = sessionRepository.findByRoomId(roomId);
        
        // Group by user and calculate totals
        Map<String, Long> userTimeMap = new HashMap<>();
        Map<String, Integer> userSessionCountMap = new HashMap<>();
        
        for (Session session : sessions) {
            userTimeMap.merge(session.getUserId(), session.getTime(), Long::sum);
            userSessionCountMap.merge(session.getUserId(), 1, Integer::sum);
        }
        
        // Create leaderboard entries
        List<LeaderboardDTO.LeaderboardEntry> entries = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : userTimeMap.entrySet()) {
            String userIdKey = entry.getKey();
            User user = userRepository.findById(userIdKey).orElse(null);
            
            if (user != null) {
                LeaderboardDTO.LeaderboardEntry leaderboardEntry = new LeaderboardDTO.LeaderboardEntry();
                leaderboardEntry.setUserId(user.getUserId());
                leaderboardEntry.setUserName(user.getName());
                leaderboardEntry.setTotalTime(entry.getValue());
                leaderboardEntry.setSessionCount(userSessionCountMap.get(userIdKey));
                leaderboardEntry.setProfileImage(user.getProfileImage());
                entries.add(leaderboardEntry);
            }
        }
        
        // Sort by total time descending
        entries.sort((a, b) -> Long.compare(b.getTotalTime(), a.getTotalTime()));
        
        // Assign ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }
        
        LeaderboardDTO.LeaderboardResponse response = new LeaderboardDTO.LeaderboardResponse();
        response.setType("ROOM");
        response.setRoomId(roomId);
        response.setRoomName(room.getRoomName());
        response.setEntries(entries);
        
        return response;
    }
    
    public LeaderboardDTO.LeaderboardResponse getTagLeaderboard(String roomId, String tagId, String userId) {
        // Verify user is member of room
        if (!connectionRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN);
        }
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException("Room not found", HttpStatus.NOT_FOUND));
        
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException("Tag not found", HttpStatus.NOT_FOUND));
        
        if (!tag.getRoomId().equals(roomId)) {
            throw new CustomException("Tag does not belong to this room", HttpStatus.BAD_REQUEST);
        }
        
        List<Session> sessions = sessionRepository.findByTagId(tagId);
        
        // Group by user and calculate totals
        Map<String, Long> userTimeMap = new HashMap<>();
        Map<String, Integer> userSessionCountMap = new HashMap<>();
        
        for (Session session : sessions) {
            userTimeMap.merge(session.getUserId(), session.getTime(), Long::sum);
            userSessionCountMap.merge(session.getUserId(), 1, Integer::sum);
        }
        
        // Create leaderboard entries
        List<LeaderboardDTO.LeaderboardEntry> entries = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : userTimeMap.entrySet()) {
            String userIdKey = entry.getKey();
            User user = userRepository.findById(userIdKey).orElse(null);
            
            if (user != null) {
                LeaderboardDTO.LeaderboardEntry leaderboardEntry = new LeaderboardDTO.LeaderboardEntry();
                leaderboardEntry.setUserId(user.getUserId());
                leaderboardEntry.setUserName(user.getName());
                leaderboardEntry.setTotalTime(entry.getValue());
                leaderboardEntry.setSessionCount(userSessionCountMap.get(userIdKey));
                leaderboardEntry.setProfileImage(user.getProfileImage());
                entries.add(leaderboardEntry);
            }
        }
        
        // Sort by total time descending
        entries.sort((a, b) -> Long.compare(b.getTotalTime(), a.getTotalTime()));
        
        // Assign ranks
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setRank(i + 1);
        }
        
        LeaderboardDTO.LeaderboardResponse response = new LeaderboardDTO.LeaderboardResponse();
        response.setType("TAG");
        response.setRoomId(roomId);
        response.setRoomName(room.getRoomName());
        response.setTagId(tagId);
        response.setTagName(tag.getTagName());
        response.setEntries(entries);
        
        return response;
    }
    
    public List<LeaderboardDTO.TagLeaderboardEntry> getUserTagStats(String userId, String roomId) {
        // Verify user is member of room
        if (!connectionRepository.existsByUserIdAndRoomId(userId, roomId)) {
            throw new CustomException("You are not a member of this room", HttpStatus.FORBIDDEN);
        }
        
        List<Session> sessions = sessionRepository.findByUserIdAndRoomId(userId, roomId);
        
        // Group by tag
        Map<String, Long> tagTimeMap = new HashMap<>();
        Map<String, Integer> tagSessionCountMap = new HashMap<>();
        
        for (Session session : sessions) {
            if (session.getTagId() != null) {
                tagTimeMap.merge(session.getTagId(), session.getTime(), Long::sum);
                tagSessionCountMap.merge(session.getTagId(), 1, Integer::sum);
            }
        }
        
        // Create tag stats
        List<LeaderboardDTO.TagLeaderboardEntry> entries = new ArrayList<>();
        
        for (Map.Entry<String, Long> entry : tagTimeMap.entrySet()) {
            String tagId = entry.getKey();
            Tag tag = tagRepository.findById(tagId).orElse(null);
            
            if (tag != null) {
                LeaderboardDTO.TagLeaderboardEntry tagEntry = new LeaderboardDTO.TagLeaderboardEntry();
                tagEntry.setTagId(tag.getTagId());
                tagEntry.setTagName(tag.getTagName());
                tagEntry.setTagColor(tag.getTagColor());
                tagEntry.setTotalTime(entry.getValue());
                tagEntry.setSessionCount(tagSessionCountMap.get(tagId));
                entries.add(tagEntry);
            }
        }
        
        // Sort by total time descending
        entries.sort((a, b) -> Long.compare(b.getTotalTime(), a.getTotalTime()));
        
        return entries;
    }
}