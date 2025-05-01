package com.dongsan.rdb.domains.walkway;

import com.dongsan.core.domains.walkway.WalkwayHistory;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rds.domains.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "walkway_history")
public class WalkwayHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkway;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private Boolean isReviewed;

    protected WalkwayHistoryEntity() {
    }

    public WalkwayHistoryEntity(Member member, WalkwayEntity walkway, Double distance, Integer time) {
        this.member = member;
        this.walkway = walkway;
        this.distance = distance;
        this.time = time;
        this.isReviewed = false;
    }

    public void updateIsReviewed(boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public Long getId() {
        return id;
    }

    public WalkwayHistory toWalkwayHistory() {
        return new WalkwayHistory(id, member.getId(), walkway.toWalkway(), distance, time, isReviewed, getCreatedAt());
    }

    public Double getDistance() {
        return distance;
    }

    public Member getMember() {
        return member;
    }

    public WalkwayEntity getWalkwayEntity() {
        return walkway;
    }

    public Integer getTime() {
        return time;
    }

    public Boolean getIsReviewed() {
        return isReviewed;
    }
}
