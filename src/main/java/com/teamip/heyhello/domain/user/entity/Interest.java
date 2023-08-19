package com.teamip.heyhello.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "interest")
@NoArgsConstructor
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    public Interest(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }
}
