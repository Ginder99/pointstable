package com.cricket.pointstable.controllers;

import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.Team;
import com.cricket.pointstable.entities.Tournament;
import com.cricket.pointstable.services.TournamentService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/{id}")
    public Tournament getTournament(@PathVariable ObjectId id) {
        // get tournament
        return tournamentService.getTournament(id);
    }

    @PostMapping
    public Tournament createTournament(@RequestBody Tournament tournament) {
        // create tournament
        return tournamentService.createTournament(tournament);
    }

    @PostMapping("{tournamentId}/teams")
    public Tournament addTeamToTournament(@PathVariable ObjectId tournamentId, @RequestBody List<Team> teams) {
        // add team to tournament
        return tournamentService.addTeamToTournament(tournamentId, teams);
    }

    @GetMapping("{tournamentId}/matches")
    public List<Match> getMatchesByTournament(@PathVariable ObjectId tournamentId) {
        // get matches by tournament
        return tournamentService.getMatchesByTournament(tournamentId);
    }
}
