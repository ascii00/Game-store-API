package com.store.game.security.implementation;

import com.store.game.models.User;
import com.store.game.models.enums.ETokenType;
import com.store.game.repositories.UserRepository;
import com.store.game.security.EmailService;
import com.store.game.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Value("${site.domain}")
    private String domain;
    private final EmailSender emailSender;

    @Override
    public void sendConfirmationEmail(User user) {
        String confirmationToken = jwtService.generateConfirmationToken(user);
        String confirmEmailLink = domain + "/api/auth/email-confirmation/" + confirmationToken;
        emailSender.sendEmail(
                user.getEmail(),
                "Confirm your email",
                "Please, confirm your email by clicking on the link: " + confirmEmailLink);
        jwtService.saveUserToken(user, confirmationToken, ETokenType.CONFIRMATION);
    }

    @Override
    public void confirmEmail(String token) {
        final String userEmail;
        userEmail = jwtService.extractUsername(token);
        if (userEmail != null) {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(token, user) && jwtService.isEmailToken(token)) {
                jwtService.revokeAllUserTokens(user, ETokenType.CONFIRMATION);
                user.setConfirmed(true);
                userRepository.save(user);
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void sendResetPasswordEmail(User user) {
        String resetPasswordToken = jwtService.generatePasswordResetToken(user);
        String resetPasswordLink = domain + "/api/user/password-reset-confirm/" + resetPasswordToken;
        emailSender.sendEmail(
                user.getEmail(),
                "Reset your password",
                "Please, reset your password by clicking on the link: " + resetPasswordLink);
        jwtService.saveUserToken(user, resetPasswordToken, ETokenType.RESET_PASSWORD);
    }
}
