package com.cricket.pointstable.controllers;

import com.cricket.pointstable.entities.Match;
import com.cricket.pointstable.entities.MatchResult;
import com.cricket.pointstable.services.MatchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping()
    public void createMatch(@RequestBody Match match) {
        matchService.createMatch(match);
    }

    @PutMapping("/result")
    public void updateMatchWithResult(@RequestBody Match match) {
        matchService.saveMatchWithResult(match);
    }

    @PostMapping("/result")
    public void publishMatchResult(@RequestBody MatchResult matchResult) {
        // matchService.publishMatchResult(matchResult);
    }
}
