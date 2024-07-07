package com.cricket.pointstable.entities;

import com.cricket.pointstable.enums.MatchResultType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matches")
@Getter
@Setter
public class Match {
    @Id
    private ObjectId id;
    private Integer matchNumber;
    private String externalMatchId;
    private String title;
    @DBRef(lazy = true)
    private Team teamOne;
    @DBRef(lazy = true)
    private Team teamTwo;
    @DBRef(lazy = true)
    private Team winner;
    @DBRef(lazy = true)
    private Tournament tournament;
    private MatchResult matchResult;
    private MatchResultType.MatchStatus matchStatus;
}
