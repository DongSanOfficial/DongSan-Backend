package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "crew_member",
        uniqueConstraints = @UniqueConstraint(columnNames = {"crewId", "memberId"}))
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
}
