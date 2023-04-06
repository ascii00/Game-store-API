package com.store.game.services.implementation;

import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.models.Role;
import com.store.game.models.User;
import com.store.game.models.enums.ERole;
import com.store.game.models.enums.ETokenType;
import com.store.game.repositories.RoleRepository;
import com.store.game.repositories.UserRepository;
import com.store.game.security.*;
import com.store.game.models.DTO.RegisterRequest;
import com.store.game.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final SecurityTools tools;

    public AuthenticationResponse register(RegisterRequest request) {
        if (!tools.isValidPassword(request.getPassword())) {
            throw new IllegalArgumentException("Password is not valid (Password must be at least 8 characters long, contain at least one number, one uppercase and one lowercase letter)");
        }

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow();

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                defaultRole
        );
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        emailService.sendConfirmationEmail(user);
        jwtService.saveUserToken(savedUser, jwtToken, ETokenType.BEARER);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            jwtService.revokeAllUserTokens(user, ETokenType.BEARER);
            jwtService.saveUserToken(user, jwtToken, ETokenType.BEARER);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}
