package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "crew_member")
public class CrewMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long crewId;

    private Long userId;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CrewMemberRole role;

    protected CrewMember() {
    }

    public CrewMember(Long crewId, Long userId, CrewMemberRole role) {
        this.crewId = crewId;
        this.userId = userId;
        this.role = role;
    }
}
