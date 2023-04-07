package com.store.game.services;

import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.models.DTO.AuthenticationResponse;
import com.store.game.models.DTO.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
