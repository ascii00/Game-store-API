package com.store.game.controllers;

import com.store.game.models.DTO.PasswordResetConfirmedRequest;
import com.store.game.models.DTO.PasswordResetRequest;
import com.store.game.response.FailResponse;
import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.security.AuthenticationRequest;
import com.store.game.security.EmailService;
import com.store.game.security.SecurityTools;
import com.store.game.security.implementation.RegisterRequest;
import com.store.game.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final EmailService emailConfirmation;
    private final SecurityTools tools;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(new SuccessResponse<>(service.register(request)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(new SuccessResponse<>(service.authenticate(request)));
    }

    @GetMapping("/email-confirmation/{token}")
    public ResponseEntity<Response> confirmEmail(@PathVariable String token){
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>("Token is empty"));
        }
        emailConfirmation.confirmEmail(token);
        return ResponseEntity.ok(new SuccessResponse<>(null));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Response> passwordReset(@RequestBody PasswordResetRequest passwordResetRequest) {
        if (service.resetPasswordRequest(passwordResetRequest)) {
            return ResponseEntity.ok().body(new SuccessResponse<>(null));
        }
        return ResponseEntity.status(NOT_FOUND).body(new FailResponse<>("User not found"));
    }

    @PostMapping("/password-reset-confirm/{token}")
    public ResponseEntity<Response> passwordResetConfirm(@PathVariable String token, @RequestBody PasswordResetConfirmedRequest passwordRequest) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>("Token is empty"));
        }
        if (!tools.isValidPassword(passwordRequest.getPassword())) {
            return ResponseEntity.status(BAD_REQUEST).body(new FailResponse<>("Password is not valid"));
        }
        if (service.resetPassword(token, passwordRequest)) {
            return ResponseEntity.ok().body(new SuccessResponse<>(null));
        }
        return ResponseEntity.status(NOT_FOUND).body(new FailResponse<>("User not found"));
    }
}
