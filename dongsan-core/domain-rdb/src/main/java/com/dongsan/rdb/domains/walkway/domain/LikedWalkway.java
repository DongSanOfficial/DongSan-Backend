package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "liked_walkway")
public class LikedWalkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long walkwayId;

    protected LikedWalkway() {
    }

    public LikedWalkway(Long memberId, Long walkwayId) {
        this.memberId = memberId;
        this.walkwayId = walkwayId;
    }

    public Long getId() {
        return id;
    }
}
