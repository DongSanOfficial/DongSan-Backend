package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
    @Override
    public String hash(String rawPassword) {
        validatePassword(rawPassword);
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8 || rawPassword.length() > 20) {
            throw new CoreException(CoreErrorCode.PRIVATE_CREW_PASSWORD_NOT_VALID);
        }
    }
}
