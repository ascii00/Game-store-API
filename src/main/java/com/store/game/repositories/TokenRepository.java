package com.store.game.repositories;

import com.store.game.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("SELECT t FROM Token t " +
            "join User u on t.user.id = u.id " +
            "WHERE u.id = :userID AND (t.expired = false or t.revoked = false )")
    List<Token> findAllValidTokensByUser(int userID);
    Optional<Token> findByToken(String token);
}
