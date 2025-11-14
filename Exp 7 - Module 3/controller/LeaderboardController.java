package org.example.timetrail.controller;

import org.example.timetrail.dto.LeaderboardDTO;
import org.example.timetrail.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {
    
    @Autowired
    private LeaderboardService leaderboardService;
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<LeaderboardDTO.LeaderboardResponse> getRoomLeaderboard(
            @PathVariable String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(leaderboardService.getRoomLeaderboard(roomId, userId));
    }
    
    @GetMapping("/room/{roomId}/tag/{tagId}")
    public ResponseEntity<LeaderboardDTO.LeaderboardResponse> getTagLeaderboard(
            @PathVariable String roomId,
            @PathVariable String tagId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(leaderboardService.getTagLeaderboard(roomId, tagId, userId));
    }
    
    @GetMapping("/user/tags")
    public ResponseEntity<List<LeaderboardDTO.TagLeaderboardEntry>> getUserTagStats(
            @RequestParam String roomId,
            Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(leaderboardService.getUserTagStats(userId, roomId));
    }
}