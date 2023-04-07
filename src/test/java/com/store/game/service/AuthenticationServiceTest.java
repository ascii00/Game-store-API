package com.store.game.service;

import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.models.DTO.AuthenticationResponse;
import com.store.game.models.DTO.RegisterRequest;
import com.store.game.models.Role;
import com.store.game.models.User;
import com.store.game.models.enums.ERole;
import com.store.game.models.enums.ETokenType;
import com.store.game.repositories.RoleRepository;
import com.store.game.repositories.UserRepository;
import com.store.game.security.EmailService;
import com.store.game.security.JwtService;
import com.store.game.security.SecurityTools;
import com.store.game.services.implementation.AuthenticationServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImp authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private EmailService emailService;
    @Mock
    private SecurityTools securityTools;

    @Test
    void register() {
        RegisterRequest request = new RegisterRequest("test@test.com", "Test123!");
        Role defaultRole = new Role(ERole.ROLE_USER);
        User user = new User("test@test.com", "Test123!", defaultRole);
        String jwtToken = "testJwtToken";

        when(securityTools.isValidPassword(request.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("Test123!");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthenticationResponse result = authenticationService.register(request);

        assertEquals(jwtToken, result.getToken());
        verify(emailService, times(1)).sendConfirmationEmail(user);
        verify(jwtService, times(1)).saveUserToken(user, jwtToken, ETokenType.BEARER);
    }

    @Test
    void registerEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("test@test.com", "Test123!");
        User existingUser = new User("test@test.com", "Test123!", null);

        when(securityTools.isValidPassword(request.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));
    }

    @Test
    void registerInvalidPassword() {
        RegisterRequest request = new RegisterRequest("test@test.com", "test");

        when(securityTools.isValidPassword(request.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));
    }

    @Test
    void authenticate() {
        AuthenticationRequest request = new AuthenticationRequest("test@test.com", "Test123!");
        Role defaultRole = new Role(ERole.ROLE_USER);
        User user = new User("test@test.com", "Test123!", defaultRole);
        String jwtToken = "testJwtToken";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthenticationResponse result = authenticationService.authenticate(request);

        assertEquals(jwtToken, result.getToken());
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        verify(jwtService, times(1)).revokeAllUserTokens(user, ETokenType.BEARER);
        verify(jwtService, times(1)).saveUserToken(user, jwtToken, ETokenType.BEARER);
    }

    @Test
    void authenticateInvalidEmailOrPassword() {
        AuthenticationRequest request = new AuthenticationRequest("test@test.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid email or password"));

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));
    }
}
