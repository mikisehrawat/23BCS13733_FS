package org.example.timetrail.repository;

import org.example.timetrail.model.JoinRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JoinRequestRepository extends MongoRepository<JoinRequest, String> {
    List<JoinRequest> findByRoomId(String roomId);
    List<JoinRequest> findByUserId(String userId);
    List<JoinRequest> findByRoomIdAndStatus(String roomId, String status);
    Optional<JoinRequest> findByUserIdAndRoomIdAndStatus(String userId, String roomId, String status);
    boolean existsByUserIdAndRoomIdAndStatus(String userId, String roomId, String status);
}