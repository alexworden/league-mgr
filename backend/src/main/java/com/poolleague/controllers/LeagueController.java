package com.poolleague.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.poolleague.services.LeagueService;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @PostMapping
    public ResponseEntity<String> createLeague(@RequestBody LeagueRequest request) {
        return ResponseEntity.ok(leagueService.createLeague(request.name, request.adminUserId));
    }
}

class LeagueRequest {
    public String name;
    public Long adminUserId;
}
