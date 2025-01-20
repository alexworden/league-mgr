package com.poolleague.services;

import com.poolleague.models.League;
import com.poolleague.models.LeagueTeam;
import com.poolleague.models.LeaguePlayer;
import com.poolleague.repositories.LeagueRepository;
import com.poolleague.repositories.LeagueTeamRepository;
import com.poolleague.repositories.LeaguePlayerRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final LeagueTeamRepository leagueTeamRepository;
    private final LeaguePlayerRepository leaguePlayerRepository;

    public LeagueService(LeagueRepository leagueRepository, 
                        LeagueTeamRepository leagueTeamRepository,
                        LeaguePlayerRepository leaguePlayerRepository) {
        this.leagueRepository = leagueRepository;
        this.leagueTeamRepository = leagueTeamRepository;
        this.leaguePlayerRepository = leaguePlayerRepository;
    }

    @Transactional
    public String createLeague(String name, String adminUserId, Integer numberOfTeams, Integer playersPerTeam) {
        League league = new League();
        league.setId(UUID.randomUUID().toString());
        league.setName(name);
        league.setAdminUserId(adminUserId);
        league.setNumberOfTeams(numberOfTeams != null ? numberOfTeams : 8);
        league.setPlayersPerTeam(playersPerTeam != null ? playersPerTeam : 3);
        league.setSignupUrl("/join/" + league.getId());
        league.setCreatedAt(LocalDateTime.now());
        league.setUpdatedAt(LocalDateTime.now());
        
        leagueRepository.save(league);
        return league.getId();
    }

    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    public League getLeagueById(String id) {
        return leagueRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("League not found with id: " + id));
    }

    @Transactional
    public League updateLeague(String id, String name, Integer numberOfTeams, Integer playersPerTeam) {
        League league = getLeagueById(id);
        if (name != null) {
            league.setName(name);
        }
        if (numberOfTeams != null) {
            league.setNumberOfTeams(numberOfTeams);
        }
        if (playersPerTeam != null) {
            league.setPlayersPerTeam(playersPerTeam);
        }
        league.setUpdatedAt(LocalDateTime.now());
        return leagueRepository.save(league);
    }

    @Transactional
    public void deleteLeague(String id) {
        League league = getLeagueById(id);
        // Delete all teams and players first
        List<LeagueTeam> teams = leagueTeamRepository.findByLeagueId(id);
        for (LeagueTeam team : teams) {
            leaguePlayerRepository.deleteAll(leaguePlayerRepository.findByTeamId(team.getId()));
        }
        leagueTeamRepository.deleteAll(teams);
        leagueRepository.delete(league);
    }

    @Transactional
    public LeagueTeam createTeam(String leagueId, String teamName) {
        League league = getLeagueById(leagueId);
        List<LeagueTeam> existingTeams = leagueTeamRepository.findByLeagueId(leagueId);
        
        if (existingTeams.size() >= league.getNumberOfTeams()) {
            throw new RuntimeException("League has reached maximum number of teams");
        }

        LeagueTeam team = new LeagueTeam();
        team.setId(UUID.randomUUID().toString());
        team.setName(teamName);
        team.setLeagueId(leagueId);
        team.setScore(0);
        team.setCreatedAt(LocalDateTime.now());
        team.setUpdatedAt(LocalDateTime.now());
        
        return leagueTeamRepository.save(team);
    }

    @Transactional
    public LeaguePlayer addPlayerToTeam(String leagueId, String teamId, String userId) {
        League league = getLeagueById(leagueId);
        LeagueTeam team = leagueTeamRepository.findById(teamId)
            .orElseThrow(() -> new RuntimeException("Team not found"));
            
        // Verify team belongs to league
        if (!team.getLeagueId().equals(leagueId)) {
            throw new RuntimeException("Team does not belong to this league");
        }

        // Check if player is already in the league
        if (leaguePlayerRepository.findByLeagueIdAndUserId(leagueId, userId).isPresent()) {
            throw new RuntimeException("Player is already in this league");
        }

        // Check if team is full
        List<LeaguePlayer> teamPlayers = leaguePlayerRepository.findByTeamId(teamId);
        if (teamPlayers.size() >= league.getPlayersPerTeam()) {
            throw new RuntimeException("Team has reached maximum number of players");
        }

        LeaguePlayer player = new LeaguePlayer();
        player.setId(UUID.randomUUID().toString());
        player.setLeagueId(leagueId);
        player.setTeamId(teamId);
        player.setUserId(userId);
        player.setScore(0);
        player.setJoinedAt(LocalDateTime.now());
        player.setUpdatedAt(LocalDateTime.now());
        
        return leaguePlayerRepository.save(player);
    }

    public List<LeagueTeam> getLeagueTeams(String leagueId) {
        return leagueTeamRepository.findByLeagueId(leagueId);
    }

    public List<LeaguePlayer> getTeamPlayers(String teamId) {
        return leaguePlayerRepository.findByTeamId(teamId);
    }

    public List<LeaguePlayer> getLeaguePlayers(String leagueId) {
        return leaguePlayerRepository.findByLeagueId(leagueId);
    }
}
