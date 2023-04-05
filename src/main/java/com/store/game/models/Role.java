package com.store.game.models;

import com.store.game.models.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "Role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name.name();
    }
}
