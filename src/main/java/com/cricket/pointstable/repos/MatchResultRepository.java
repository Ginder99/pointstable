package com.cricket.pointstable.repos;

import com.cricket.pointstable.entities.MatchResult;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchResultRepository extends MongoRepository<MatchResult, ObjectId> {
    Page<MatchResult> findByAddToPointsTable(boolean addToPointsTable, Pageable pageRequest);
}
