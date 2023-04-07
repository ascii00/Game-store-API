package com.store.game.controllers;

import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.models.DTO.RegisterRequest;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.models.DTO.AuthenticationResponse;
import com.store.game.security.EmailService;
import com.store.game.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private EmailService emailConfirmation;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("test@example.com", "password123");
        authenticationRequest = new AuthenticationRequest("test@example.com", "password123");
        authenticationResponse = AuthenticationResponse.builder().token("jwtToken").build();
    }

    @Test
    void registerTest() {
        when(authenticationService.register(registerRequest)).thenReturn(authenticationResponse);

        ResponseEntity<Response> response = authenticationController.register(registerRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, ((SuccessResponse<AuthenticationResponse>) response.getBody()).getData());
        verify(authenticationService, times(1)).register(registerRequest);
    }

    @Test
    void authenticateTest() {
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        ResponseEntity<Response> response = authenticationController.authenticate(authenticationRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(authenticationResponse, ((SuccessResponse<AuthenticationResponse>) response.getBody()).getData());
        verify(authenticationService, times(1)).authenticate(authenticationRequest);
    }

    @Test
    void confirmEmailTest() {
        String token = "token";
        doNothing().when(emailConfirmation).confirmEmail(token);
        ResponseEntity<Response> response = authenticationController.confirmEmail(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(emailConfirmation, times(1)).confirmEmail(token);
    }
}

