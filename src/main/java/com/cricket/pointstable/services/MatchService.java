package com.cricket.pointstable.services;

import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.MatchResult;
import com.cricket.pointstable.entities.Team;
import com.cricket.pointstable.entities.Tournament;
import com.cricket.pointstable.enums.MatchResultType;
import com.cricket.pointstable.repos.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.cricket.pointstable.enums.MatchResultType.NO_RESULT;

@Slf4j
@Service
public class MatchService {
    private final MatchRepository matchRepository;

    private final TeamService teamService;

    private final MatchResultService matchResultService;

    public MatchService(MatchRepository matchRepository, TeamService teamService, MatchResultService matchResultService) {
        this.matchRepository = matchRepository;
        this.teamService = teamService;
        this.matchResultService = matchResultService;
    }

    public void createMatch(Match match) {
        matchRepository.save(match);
    }

    public void saveMatchWithResult(Match match) {
        Optional<Match> existingMatchOpt = matchRepository.findByExternalMatchId(match.getExternalMatchId());
        try {
            existingMatchOpt.ifPresent(existingmatch -> {
                MatchResult matchResult = match.getMatchResult();
                existingmatch.setMatchResult(matchResult);
                Team teamOne = teamService.findByName(match.getTeamOne().getName()).orElseThrow(() -> new RuntimeException("Add Team " + match.getTeamOne().getName() + " to the tournament first"));
                existingmatch.setTeamOne(teamOne);
                Team teamTwo = teamService.findByName(match.getTeamTwo().getName()).orElseThrow(() -> new RuntimeException("Add Team " + match.getTeamTwo().getName() + " to the tournament first"));
                existingmatch.setTeamTwo(teamTwo);
                if(!NO_RESULT.equals(matchResult.getMatchResultType())) {
                    Team winner = teamService.findByName(match.getWinner().getName()).orElseThrow(()
                            -> new RuntimeException("Add Team " + match.getWinner().getName() + " to the tournament first"));
                    existingmatch.setWinner(winner);
                }
                existingmatch.setMatchStatus(match.getMatchStatus());
                matchRepository.save(existingmatch);
            });
        } catch (Exception e) {
            log.error("Error while saving match: {}", match.getExternalMatchId(), e);
        }

    }

    public List<Match> getAllByTournament(ObjectId tournamentId) {
        Tournament tournament = new Tournament();
        tournament.setId(tournamentId);
        return matchRepository.findAllByTournamentOrderByMatchNumber(tournament);
    }

    // TODO: Not Working as intended
    public Page<Match> getMatchesWithAddToPointsTableTrue(boolean addToPointsTable, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("matchNumber"));
        return matchRepository.findByMatchResultAddToPointsTable(addToPointsTable, pageable);
    }

    public Match findById(ObjectId matchId) {
        return matchRepository.findById(matchId).orElseThrow(() -> new RuntimeException("Match not found for id: " + matchId));
    }

    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }
}
