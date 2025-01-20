package com.poolleague.repositories;

import com.poolleague.models.LeaguePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaguePlayerRepository extends JpaRepository<LeaguePlayer, String> {
    List<LeaguePlayer> findByLeagueId(String leagueId);
    List<LeaguePlayer> findByTeamId(String teamId);
    List<LeaguePlayer> findByUserId(String userId);
    Optional<LeaguePlayer> findByLeagueIdAndUserId(String leagueId, String userId);
}
