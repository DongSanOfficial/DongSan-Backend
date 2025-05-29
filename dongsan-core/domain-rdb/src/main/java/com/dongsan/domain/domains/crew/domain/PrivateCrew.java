package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PRIVATE")
public class PrivateCrew extends Crew {
    @Column(name = "password")
    private String hashedPassword;

    protected PrivateCrew() {
    }

    public PrivateCrew(String name, String description, String rule, String crewImageUrl, Capacity capacity, String hashedPassword) {
        super(name, description, rule, crewImageUrl, capacity);

        validatePassword(hashedPassword);
        this.hashedPassword = hashedPassword;
    }

    private void validatePassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new CoreException(CoreErrorCode.PRIVATE_CREW_PASSWORD_NOT_VALID);
        }
    }


}
