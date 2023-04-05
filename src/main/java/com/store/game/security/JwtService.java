package com.store.game.security;

import com.store.game.models.User;
import com.store.game.models.enums.ETokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    String generateConfirmationToken(UserDetails userDetails);
    void revokeAllUserTokens(User user, ETokenType tokenType);
    boolean isTokenValid(String jwt, UserDetails userDetails);
    boolean isEmailToken(String jwt);
    void saveUserToken(User user, String jwtToken, ETokenType tokenType);
}
