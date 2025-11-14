package org.example.timetrail.repository;

import org.example.timetrail.model.Connection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends MongoRepository<Connection, String> {
    List<Connection> findByUserId(String userId);
    List<Connection> findByRoomId(String roomId);
    Optional<Connection> findByUserIdAndRoomId(String userId, String roomId);
    boolean existsByUserIdAndRoomId(String userId, String roomId);
    void deleteByUserIdAndRoomId(String userId, String roomId);
    long countByRoomId(String roomId);
}