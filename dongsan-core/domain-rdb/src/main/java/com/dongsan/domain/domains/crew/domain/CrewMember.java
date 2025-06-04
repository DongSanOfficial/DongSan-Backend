package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.domains.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "crew_member")
public class CrewMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long crewId;

    private Long memberId;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CrewMemberRole role;

    protected CrewMember() {
    }

    public CrewMember(Long crewId, Long memberId, CrewMemberRole role) {
        this.crewId = crewId;
        this.memberId = memberId;
        this.role = role;
    }

    public boolean isManager() {
        return role.equals(CrewMemberRole.MANAGER);
    }

    public Long getCrewId() {
        return crewId;
    }
}
