package com.cricket.pointstable.repos;

import com.cricket.pointstable.entities.Tournament;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, ObjectId> {
    Optional<Tournament> findByName(String name);
}
