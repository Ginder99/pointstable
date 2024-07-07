package com.cricket.pointstable.repos;

import com.cricket.pointstable.entities.Team;
import org.apache.zookeeper.Op;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, ObjectId> {
    Optional<Team> findByName(String name);
}
