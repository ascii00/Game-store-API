package com.store.game.services;

import com.store.game.models.DTO.PasswordResetConfirmedRequest;
import com.store.game.models.DTO.PasswordResetRequest;
import com.store.game.models.Role;
import com.store.game.models.enums.ERole;
import com.store.game.security.AuthenticationRequest;
import com.store.game.security.AuthenticationResponse;
import com.store.game.security.implementation.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    Role getRole(ERole name);
    boolean resetPasswordRequest(PasswordResetRequest passwordResetRequest);
    boolean resetPassword(String token, PasswordResetConfirmedRequest passwordRequest);
}
