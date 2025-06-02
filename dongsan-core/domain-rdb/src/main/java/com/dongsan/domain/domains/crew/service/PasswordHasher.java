package com.dongsan.domain.domains.crew.service;

import org.springframework.stereotype.Component;

@Component
public interface PasswordHasher {
    String hash(String rawPassword);

    boolean verify(String rawPassword, String hashedPassword);
}
