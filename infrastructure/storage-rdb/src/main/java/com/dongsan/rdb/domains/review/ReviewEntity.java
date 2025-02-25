package com.dongsan.rdb.domains.review;

import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.Reviewer;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayHistoryEntity;
import jakarta.persistence.*;

@Entity
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkwayEntity;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private WalkwayHistoryEntity walkwayHistoryEntity;

    protected ReviewEntity() {}

    public ReviewEntity(Integer rating, String content, MemberEntity memberEntity, WalkwayEntity walkwayEntity, WalkwayHistoryEntity walkwayHistoryEntity){
        this.rating = rating;
        this.content = content;

        // 연관관계 매핑
        this.memberEntity = memberEntity;
        this.walkwayEntity = walkwayEntity;
        this.walkwayHistoryEntity = walkwayHistoryEntity;
    }

    public Review toReview() {
        return new Review(id, new Reviewer(memberEntity.getId(), memberEntity.getNickname()), walkwayEntity.toReviewedWalkway(), rating, content, getCreatedAt());
    }

    public Long getId() {
        return id;
    }
}
