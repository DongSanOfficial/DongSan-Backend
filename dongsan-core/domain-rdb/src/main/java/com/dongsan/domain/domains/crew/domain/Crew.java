package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "crew")
public class Crew extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private CrewInfo info;

    @Embedded
    private Capacity capacity;

    @Embedded
    private CrewAccessPolicy accessPolicy;

    protected Crew() {
    }

    public Crew(CrewInfo info, Capacity capacity, CrewAccessPolicy accessPolicy) {
        this.info = info;
        this.capacity = capacity;
        this.accessPolicy = accessPolicy;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return info.getName();
    }

    public String getDescription() {
        return info.getDescription();
    }

    public String getRule() {
        return info.getRule();
    }

    public String getCrewImageUrl() {
        return info.getCrewImageUrl();
    }

    public Integer getMemberLimit() {
        return capacity.getMemberLimit();
    }

    public String getCrewType() {
        return accessPolicy.getCrewExposeLevel().name();
    }

    public String getPassword() {
        return accessPolicy.getHashedPassword();
    }

    public void validateNotFull(int memberCount) {
        capacity.validateNotFull(memberCount);
    }

    public boolean isLimitedCrew() {
        return capacity.isLimitedCrew();
    }

    public void canAccess(boolean isCrewMember) {
        accessPolicy.canAccess(isCrewMember);
    }

    public boolean needsPassword() {
        return accessPolicy.needsPassword();
    }

    public void update(CrewInfo info, Capacity capacity, CrewAccessPolicy accessPolicy) {
        this.info = info;
        this.capacity = capacity;
        this.accessPolicy = accessPolicy;
    }
}
