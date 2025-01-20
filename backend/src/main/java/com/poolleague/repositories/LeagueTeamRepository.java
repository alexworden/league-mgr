package com.poolleague.repositories;

import com.poolleague.models.LeagueTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeagueTeamRepository extends JpaRepository<LeagueTeam, String> {
    List<LeagueTeam> findByLeagueId(String leagueId);
}
