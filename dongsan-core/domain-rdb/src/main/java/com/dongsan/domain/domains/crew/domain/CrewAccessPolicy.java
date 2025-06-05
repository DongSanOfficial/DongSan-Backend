package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class CrewAccessPolicy {
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false, name = "crew_type")
    private CrewExposeLevel crewExposeLevel;

    @Column(name = "password")
    private String hashedPassword;

    protected CrewAccessPolicy() {
    }

    private CrewAccessPolicy(CrewExposeLevel crewExposeLevel, String hashedPassword) {
        this.crewExposeLevel = crewExposeLevel;
        this.hashedPassword = hashedPassword;
    }

    public static CrewAccessPolicy publicCrew() {
        return new CrewAccessPolicy(CrewExposeLevel.PUBLIC, null);
    }

    public static CrewAccessPolicy privateCrew(String hashedPassword) {
        validatePassword(hashedPassword);
        return new CrewAccessPolicy(CrewExposeLevel.PRIVATE, hashedPassword);
    }

    private static void validatePassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new CoreException(CoreErrorCode.PRIVATE_CREW_PASSWORD_NOT_VALID);
        }
    }

    public void canAccess(boolean isCrewMember) {
        if (crewExposeLevel.equals(CrewExposeLevel.PRIVATE) && !isCrewMember) {
            throw new CoreException(CoreErrorCode.CREW_CANT_ACCESS);
        }
    }

    public boolean needsPassword() {
        return crewExposeLevel.equals(CrewExposeLevel.PRIVATE);
    }

    public CrewExposeLevel getCrewExposeLevel() {
        return crewExposeLevel;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
