package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "liked_walkway")
public class LikedWalkwayEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkway;

    protected LikedWalkwayEntity() {
    }

    // 생성 시 연관관계 매핑 진행
    public LikedWalkwayEntity(Member member, WalkwayEntity walkway) {
        this.member = member;
        this.walkway = walkway;
    }

    public Long getId() {
        return id;
    }

    public Member getMemberEntity() {
        return member;
    }

    public WalkwayEntity getWalkwayEntity() {
        return walkway;
    }
}
