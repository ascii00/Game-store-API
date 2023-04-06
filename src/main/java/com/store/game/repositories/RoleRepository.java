package com.store.game.repositories;

import com.store.game.models.Role;
import com.store.game.models.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
