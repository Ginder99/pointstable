package com.cricket.pointstable.services;

import com.cricket.pointstable.entities.MatchResult;
import com.cricket.pointstable.entities.Team;
import com.cricket.pointstable.entities.Tournament;
import com.cricket.pointstable.repos.TeamRepository;
import com.cricket.pointstable.repos.TournamentRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeamService {
    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team createTeam(Team team) {
        return findByName(team.getName()).orElseGet(() -> teamRepository.save(team));
    }

    public Optional<Team> findByName(String name) {
        return teamRepository.findByName(name);
    }

    public Team getTeam(ObjectId id) {
        return teamRepository.findById(id).orElse(null);
    }
}
