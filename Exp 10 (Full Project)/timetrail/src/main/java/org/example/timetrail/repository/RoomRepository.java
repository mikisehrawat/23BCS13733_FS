package org.example.timetrail.repository;

import org.example.timetrail.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findByCreatedBy(String userId);
    List<Room> findByIsActive(boolean isActive);
}