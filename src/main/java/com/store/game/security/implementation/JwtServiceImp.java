package com.store.game.security.implementation;

import com.store.game.models.Token;
import com.store.game.models.User;
import com.store.game.models.enums.ETokenType;
import com.store.game.repositories.TokenRepository;
import com.store.game.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImp implements JwtService {

    private final TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    private static final long EXPIRATION_TIME_LOGIN = 1000 * 60 * 24 * 10; // s * m * h * d
    private static final long EXPIRATION_TIME_CONFIRM = 1000 * 60 * 24 * 10; // s * m * h * d

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_LOGIN))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateConfirmationToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_CONFIRM))
                .claim("token_type", "email_confirmation")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public void revokeAllUserTokens(User user, ETokenType tokenType) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            if (token.getTokenType().equals(tokenType)) {
                token.setExpired(true);
                token.setRevoked(true);
            }
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        boolean tokenDatabaseCheck = tokenRepository
                .findByToken(jwt)
                .map(token -> !token.isRevoked() && !token.isExpired())
                .orElse(false);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
        return tokenDatabaseCheck && isValid;
    }

    @Override
    public void saveUserToken(User user, String jwtToken, ETokenType tokenType) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public boolean isEmailToken(String jwt) {
        Claims claims = extractAllClaims(jwt);
        return claims.get("token_type", String.class).equals("email_confirmation");
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
