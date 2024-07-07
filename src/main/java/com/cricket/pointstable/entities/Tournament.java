package com.cricket.pointstable.entities;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "tournaments")
@Getter
@Setter
public class Tournament {
    @Id
    private ObjectId id;
    private String name;
    @DBRef(lazy = true)
    private List<Team> participatingTeams;
    private PointsTable pointsTable;
}
