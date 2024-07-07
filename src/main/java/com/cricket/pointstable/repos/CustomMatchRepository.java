package com.cricket.pointstable.repos;

import com.cricket.pointstable.entities.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMatchRepository {
    Page<Match> findMatchesByAddToPointsTableTrue(Pageable pageable);
}
