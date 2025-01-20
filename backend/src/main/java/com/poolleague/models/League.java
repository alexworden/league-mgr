package com.poolleague.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "leagues")
public class League {
    @Id
    private String id;
    private String name;
    private String adminUserId;
    private Integer numberOfTeams;
    private Integer playersPerTeam;
    private String signupUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public League() {
        this.numberOfTeams = numberOfTeams != null ? numberOfTeams : 8;
        this.playersPerTeam = playersPerTeam != null ? playersPerTeam : 3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Integer getNumberOfTeams() {
        return numberOfTeams;
    }

    public void setNumberOfTeams(Integer numberOfTeams) {
        this.numberOfTeams = numberOfTeams;
    }

    public Integer getPlayersPerTeam() {
        return playersPerTeam;
    }

    public void setPlayersPerTeam(Integer playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

    public String getSignupUrl() {
        return signupUrl;
    }

    public void setSignupUrl(String signupUrl) {
        this.signupUrl = signupUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
