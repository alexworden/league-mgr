package com.poolleague.controllers;

import com.poolleague.models.League;
import com.poolleague.models.LeagueTeam;
import com.poolleague.models.LeaguePlayer;
import com.poolleague.services.LeagueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/leagues")
@CrossOrigin(origins = "http://localhost:3000")
public class LeagueController {
    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @PostMapping
    public ResponseEntity<String> createLeague(@RequestBody LeagueRequest request) {
        try {
            String leagueId = leagueService.createLeague(
                request.name,
                request.adminUserId,
                request.numberOfTeams,
                request.playersPerTeam
            );
            return ResponseEntity.ok(leagueId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<League>> getAllLeagues() {
        try {
            List<League> leagues = leagueService.getAllLeagues();
            return ResponseEntity.ok(leagues);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<League> getLeagueById(@PathVariable String id) {
        try {
            League league = leagueService.getLeagueById(id);
            return ResponseEntity.ok(league);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<League> updateLeague(@PathVariable String id, @RequestBody LeagueRequest request) {
        try {
            League updatedLeague = leagueService.updateLeague(
                id,
                request.name,
                request.numberOfTeams,
                request.playersPerTeam
            );
            return ResponseEntity.ok(updatedLeague);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable String id) {
        try {
            leagueService.deleteLeague(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{leagueId}/teams")
    public ResponseEntity<LeagueTeam> createTeam(
            @PathVariable String leagueId,
            @RequestBody CreateTeamRequest request) {
        try {
            LeagueTeam team = leagueService.createTeam(leagueId, request.name);
            return ResponseEntity.ok(team);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{leagueId}/teams/{teamId}/players")
    public ResponseEntity<LeaguePlayer> addPlayerToTeam(
            @PathVariable String leagueId,
            @PathVariable String teamId,
            @RequestBody AddPlayerRequest request) {
        try {
            LeaguePlayer player = leagueService.addPlayerToTeam(leagueId, teamId, request.userId);
            return ResponseEntity.ok(player);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{leagueId}/teams")
    public ResponseEntity<List<LeagueTeam>> getLeagueTeams(@PathVariable String leagueId) {
        try {
            List<LeagueTeam> teams = leagueService.getLeagueTeams(leagueId);
            return ResponseEntity.ok(teams);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{leagueId}/players")
    public ResponseEntity<List<LeaguePlayer>> getLeaguePlayers(@PathVariable String leagueId) {
        try {
            List<LeaguePlayer> players = leagueService.getLeaguePlayers(leagueId);
            return ResponseEntity.ok(players);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<List<LeaguePlayer>> getTeamPlayers(@PathVariable String teamId) {
        try {
            List<LeaguePlayer> players = leagueService.getTeamPlayers(teamId);
            return ResponseEntity.ok(players);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

class LeagueRequest {
    public String name;
    public String adminUserId;
    public Integer numberOfTeams;
    public Integer playersPerTeam;
}

class CreateTeamRequest {
    public String name;
}

class AddPlayerRequest {
    public String userId;
}
