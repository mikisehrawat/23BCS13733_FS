package org.example.timetrail.repository;

import org.example.timetrail.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    List<Tag> findByRoomId(String roomId);
    boolean existsByTagNameAndRoomId(String tagName, String roomId);
}