package com.poolleague.models;

import jakarta.persistence.*;

@Entity
@Table(name = "leagues")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "admin_user_id", nullable = false)
    private User adminUser;

    public League() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }
}
