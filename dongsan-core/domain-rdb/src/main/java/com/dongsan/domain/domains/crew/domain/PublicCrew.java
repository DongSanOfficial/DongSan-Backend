package com.dongsan.domain.domains.crew.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PUBLIC")
public class PublicCrew extends Crew {

    protected PublicCrew() {
    }

    public PublicCrew(String name, String description, String rule, String crewImageUrl, Capacity capacity) {
        super(name, description, rule, crewImageUrl, capacity);
    }

    @Override
    public void canAccess(boolean isCrewMember) {
        // 공개 크루는 무조건 접근 가능
    }

    @Override
    public String provideVisibility() {
        return CrewExposeLevel.PUBLIC.toString();
    }
}
