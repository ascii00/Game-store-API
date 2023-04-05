package com.store.game.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityTools {
    public boolean isValidPhone(String phone) {
        return phone.matches("^\\+?[0-9\\s]*$");
    }

    public boolean isValidPassword(String password) {
        if(password == null || password.length() < 8) {
            return false;
        }
        if(!password.matches(".*[A-Z].*")) {
            return false;
        }
        if(!password.matches(".*[a-z].*")) {
            return false;
        }
        if(!password.matches(".*[0-9].*")) {
            return false;
        }
        return password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?].*");
    }
}
