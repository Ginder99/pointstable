package com.cricket.pointstable.services;

import com.cricket.pointstable.dtos.MatchResultKafkaMessage;
import com.cricket.pointstable.entities.*;
import com.cricket.pointstable.enums.MatchResultType;
import com.cricket.pointstable.repos.TournamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TeamService teamService;
    private final MatchService matchService;

    private final ObjectMapper objectMapper;

    public TournamentService(TournamentRepository tournamentRepository, TeamService teamService, MatchService matchService, ObjectMapper objectMapper) {
        this.tournamentRepository = tournamentRepository;
        this.teamService = teamService;
        this.matchService = matchService;
        this.objectMapper = objectMapper;
    }

    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.findByName(tournament.getName()).orElseGet(() -> tournamentRepository.save(tournament));
    }

    public Tournament getTournament(ObjectId id) {
        return tournamentRepository.findById(id).orElse(null);
    }
    @Synchronized
    public Tournament addTeamToTournament(ObjectId tournamentId, List<Team> teamList) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new RuntimeException("Tournament not found"));
        List<Team> teams = tournament.getParticipatingTeams();
        log.info("Teams: {}", teams);
        if(teams == null) {
            teams = teamList;
        } else {
            teams.addAll(teamList);
        }
        tournament.setParticipatingTeams(teams);
        log.info("Added teams to tournament: {}", teams);
        return tournamentRepository.save(tournament);
    }
    public Match processMatchResult(MatchResultKafkaMessage matchResult) {
        Match match = matchService.findById(matchResult.getMatchId());
        Tournament tournament = match.getTournament();
        PointsTable pointsTable = tournament.getPointsTable();
        if(pointsTable == null) {
            pointsTable = new PointsTable();
        }
        tournament.setPointsTable(updatePointsTable(pointsTable, matchResult, match));
        tournamentRepository.save(tournament);
        matchResult.setAddToPointsTable(false);
        return matchService.saveMatch(match);
    }

    private PointsTable updatePointsTable(PointsTable pointsTable, MatchResultKafkaMessage matchResult, Match match) {
        List<PointsTable.PointsTableEntry> entries = pointsTable.getPointsTableEntries();
        if(entries == null) {
            entries = new ArrayList<>();
            pointsTable.setPointsTableEntries(entries);
        }
        Team teamOne = match.getTeamOne();
        Team teamTwo = match.getTeamTwo();
        Optional<PointsTable.PointsTableEntry> teamOneEntryOptional = entries.stream().filter(pointsTableEntry -> teamOne.equals(pointsTableEntry.getTeam())).findFirst();
        PointsTable.PointsTableEntry teamOneEntry;
        if(teamOneEntryOptional.isEmpty()) {
            teamOneEntry = buildNewEntry(entries, teamOne);
        } else {
            teamOneEntry = teamOneEntryOptional.get();
        }
        log.info("Team One Entry before processing match number: {}, :{}", match.getMatchNumber(), objectMapper.valueToTree(teamOneEntry));
        teamOneEntry.setMatchesPlayed(teamOneEntry.getMatchesPlayed() + 1);
        teamOneEntry.setPoints(teamOneEntry.getPoints() + matchResult.getTeamOneScore().getPoints());
        Optional<PointsTable.PointsTableEntry> teamTwoEntryOptional = entries.stream().filter(pointsTableEntry -> teamTwo.equals(pointsTableEntry.getTeam())).findFirst();
        PointsTable.PointsTableEntry teamTwoEntry;
        if(teamTwoEntryOptional.isEmpty()) {
            teamTwoEntry = buildNewEntry(entries, teamTwo);
        } else {
            teamTwoEntry = teamTwoEntryOptional.get();
        }
        log.info("Team Two Entry before processing match number: {}, :{}", match.getMatchNumber(), objectMapper.valueToTree(teamTwoEntry));
        teamTwoEntry.setMatchesPlayed(teamTwoEntry.getMatchesPlayed() + 1);
        teamTwoEntry.setPoints(teamTwoEntry.getPoints() + matchResult.getTeamTwoScore().getPoints());
        if(MatchResultType.NO_RESULT.equals(matchResult.getMatchResultType())) {
            log.info(match.getTitle() + " is a no result");
            teamOneEntry.setMatchesNoResult(teamOneEntry.getMatchesNoResult() + 1);
            teamTwoEntry.setMatchesNoResult(teamTwoEntry.getMatchesNoResult() + 1);
        } else if(teamOne.equals(match.getWinner())) {
            teamOneEntry.setMatchesWon(teamOneEntry.getMatchesWon() + 1);
            teamTwoEntry.setMatchesLost(teamTwoEntry.getMatchesLost() + 1);
        } else {
            teamTwoEntry.setMatchesWon(teamTwoEntry.getMatchesWon() + 1);
            teamOneEntry.setMatchesLost(teamOneEntry.getMatchesLost() + 1);
        }
        matchResult.getMatchResultType().calculateNetRunRate(teamOneEntry, teamTwoEntry, matchResult);
        log.info("Team One Entry after processing match number: {}, :{}", match.getMatchNumber(), objectMapper.valueToTree(teamOneEntry));
        log.info("Team Two Entry after processing match number: {}, :{}", match.getMatchNumber(), objectMapper.valueToTree(teamTwoEntry));
        Collections.sort(entries);
        return pointsTable;
    }

    private PointsTable.PointsTableEntry buildNewEntry(List<PointsTable.PointsTableEntry> entries, Team teamOne) {
        PointsTable.PointsTableEntry entry = new PointsTable.PointsTableEntry();
        entry.setTeam(teamOne);
        entries.add(entry);
        return entry;
    }

    public List<Match> getMatchesByTournament(ObjectId tournamentId) {
        return matchService.getAllByTournament(tournamentId);
    }
}
