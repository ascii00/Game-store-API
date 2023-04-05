package com.store.game.controllers;

import com.store.game.response.Response;
import com.store.game.response.SuccessResponse;
import com.store.game.models.DTO.AuthenticationRequest;
import com.store.game.security.EmailService;
import com.store.game.models.DTO.RegisterRequest;
import com.store.game.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final EmailService emailConfirmation;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(new SuccessResponse<>(authenticationService.register(request)));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(new SuccessResponse<>(authenticationService.authenticate(request)));
    }

    @GetMapping("/email-confirmation/{token}")
    public ResponseEntity<Response> confirmEmail(@PathVariable String token){
        emailConfirmation.confirmEmail(token);
        return ResponseEntity.ok(new SuccessResponse<>(null));
    }
}
