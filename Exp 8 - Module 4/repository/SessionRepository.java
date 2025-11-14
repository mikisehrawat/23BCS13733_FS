package org.example.timetrail.repository;

import org.example.timetrail.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    List<Session> findByUserId(String userId);
    List<Session> findByRoomId(String roomId);
    List<Session> findByUserIdAndRoomId(String userId, String roomId);
    List<Session> findByTagId(String tagId);
    
    @Query("{ 'userId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<Session> findByUserIdAndDateRange(String userId, LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'roomId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<Session> findByRoomIdAndDateRange(String roomId, LocalDateTime start, LocalDateTime end);
}