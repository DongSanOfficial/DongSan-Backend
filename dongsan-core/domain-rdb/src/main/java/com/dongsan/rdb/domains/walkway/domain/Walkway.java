package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.domains.member.Member;
import com.dongsan.rdb.domains.walkway.ListStringConverter;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import jakarta.persistence.*;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "walkway")
public class Walkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double distance;

    @Column(nullable = false)
    private Integer time; // 초

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExposeLevel exposeLevel;

    @Column(nullable = false)
    private Point startLocation;

    @Column(nullable = false)
    private Point endLocation;

    private String memo;

    @Column(nullable = false)
    private Integer likeCount;

    @Column(nullable = false)
    private Integer reviewCount;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private LineString course;

    private String courseImageUrl;

    @Convert(converter = ListStringConverter.class)
    private List<String> hashtags;

    protected Walkway() {
    }

    // 연관관계 매핑도 생성 시 매핑합니다.
    public Walkway(String name, Double distance, Integer time, ExposeLevel exposeLevel, Point startLocation,
                   Point endLocation, String memo, LineString course, String courseImageUrl, Long memberId,
                   List<String> hashtags) {
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.exposeLevel = exposeLevel;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.memo = memo;
        this.course = course;
        this.courseImageUrl = courseImageUrl;
        this.hashtags = hashtags;

        // 연관관계 매핑
        this.memberId = memberId;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public void updateRatingAndReviewCount(Double rating, Integer reviewCount) {
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void updateWalkway(String name, String memo, ExposeLevel exposeLevel, List<String> hashtags) {
        this.name = name;
        this.memo = memo;
        this.exposeLevel = exposeLevel;
        this.hashtags = hashtags;
    }

    public void validateAccess(Long memberId) {
        validateOwner(memberId);
        validateExposeLevel();
    }

    private void validateOwner(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new CoreException(CoreErrorCode.NOT_WALKWAY_OWNER);
        }
    }

    private void validateExposeLevel() {
        if (this.exposeLevel.equals(ExposeLevel.PRIVATE)) {
            throw new CoreException(CoreErrorCode.WALKWAY_PRIVATE);
        }
    }

    public Long getId() {
        return id;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public Double getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public Double getDistance() {
        return distance;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public Member getMember() {
        return member;
    }

    public ExposeLevel getExposeLevel() {
        return exposeLevel;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }
}
