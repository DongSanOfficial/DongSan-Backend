package com.dongsan.rdb.domains.review;

import com.dongsan.core.domains.review.Rating;
import com.dongsan.core.domains.review.Review;
import com.dongsan.core.domains.review.Reviewer;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import com.dongsan.rdb.domains.walkway.WalkwayEntity;
import com.dongsan.rdb.domains.walkway.WalkwayHistoryEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "review")
public class ReviewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walkway_id")
    private WalkwayEntity walkway;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private WalkwayHistoryEntity walkwayHistory;

    protected ReviewEntity() {}

    public ReviewEntity(Rating rating, String content, MemberEntity member, WalkwayEntity walkway, WalkwayHistoryEntity walkwayHistory){
        this.rating = rating.getValue();
        this.content = content;

        // 연관관계 매핑
        this.member = member;
        this.walkway = walkway;
        this.walkwayHistory = walkwayHistory;
    }

    public Review toReview() {
        return new Review(id, new Reviewer(member.getId(), member.getNickname()), walkway.toReviewedWalkway(), Rating.valueOf(rating), content, getCreatedAt());
    }

    public Long getId() {
        return id;
    }
}
