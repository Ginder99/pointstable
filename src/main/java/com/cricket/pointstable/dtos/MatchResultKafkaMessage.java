package com.cricket.pointstable.dtos;

import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.MatchResult;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class MatchResultKafkaMessage extends MatchResult {
    private ObjectId matchId;
}
