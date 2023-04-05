package com.store.game.security;

import com.store.game.models.User;

public interface EmailService {
    void sendConfirmationEmail(User user);
    void confirmEmail(String token);
    void sendResetPasswordEmail(User user);
}
