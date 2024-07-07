package com.cricket.pointstable.repos;

import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.Tournament;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends MongoRepository<Match, ObjectId> {
    List<Match> findAllByTournamentOrderByMatchNumber(Tournament tournament);

    Optional<Match> findByExternalMatchId(String externalMatchId);

    Page<Match> findByMatchResultAddToPointsTable(boolean addToPointsTable, Pageable pageable);
}
