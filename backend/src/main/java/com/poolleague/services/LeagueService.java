package com.poolleague.services;

import org.springframework.stereotype.Service;
import com.poolleague.repositories.LeagueRepository;
import com.poolleague.repositories.UserRepository;
import com.poolleague.models.League;
import com.poolleague.models.User;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final UserRepository userRepository;

    public LeagueService(LeagueRepository leagueRepository, UserRepository userRepository) {
        this.leagueRepository = leagueRepository;
        this.userRepository = userRepository;
    }

    public String createLeague(String name, Long adminUserId) {
        User adminUser = userRepository.findById(adminUserId)
            .orElseThrow(() -> new RuntimeException("Admin user not found"));

        League league = new League();
        league.setName(name);
        league.setAdminUser(adminUser);
        
        leagueRepository.save(league);
        return "League created successfully!";
    }
}
