package com.cricket.pointstable.controllers;

import com.cricket.pointstable.entities.Team;
import com.cricket.pointstable.services.TeamService;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable ObjectId id) {
        return teamService.getTeam(id);
    }

    @PostMapping
    public Team createTeam(@RequestBody Team team) {
        return teamService.createTeam(team);
    }
}
