package com.cricket.pointstable.entities;

import com.cricket.pointstable.enums.MatchResultType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
public class MatchResult {
    private MatchResultType matchResultType;
    private Score teamOneScore;
    private Score teamTwoScore;
    private boolean addToPointsTable;
}
