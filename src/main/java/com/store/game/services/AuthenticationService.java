package com.store.game.services;

import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.security.AuthenticationResponse;
import com.store.game.models.DTO.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
