package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.domains.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "liked_walkway")
public class LikedWalkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private Walkway walkway;

    protected LikedWalkway() {
    }

    // 생성 시 연관관계 매핑 진행
    public LikedWalkway(Member member, Walkway walkway) {
        this.member = member;
        this.walkway = walkway;
    }

    public Long getId() {
        return id;
    }

    public Member getMemberEntity() {
        return member;
    }

    public Walkway getWalkwayEntity() {
        return walkway;
    }
}
