package com.dongsan.rdb.domains.walkway.entity;

import com.dongsan.core.domains.review.ReviewedWalkway;
import com.dongsan.core.support.util.Author;
import com.dongsan.core.domains.walkway.CourseInfo;
import com.dongsan.core.domains.walkway.CreateWalkway;
import com.dongsan.core.domains.walkway.Stat;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "walkway")
public class WalkwayEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

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

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> hashtags;

    protected WalkwayEntity() {}

    // 연관관계 매핑도 생성 시 매핑합니다.
    public WalkwayEntity(String name, Double distance, Integer time, ExposeLevel exposeLevel, Point startLocation,
                          Point endLocation, String memo, LineString course, String courseImageUrl, MemberEntity member, List<String> hashtags){
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
        this.member = member;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public WalkwayEntity(CreateWalkway createWalkway, MemberEntity member){
        // 경로
        LineString course = createWalkway.course();
        Point startLocation = createWalkway.startLocation();
        Point endLocation = createWalkway.endLocation();

        course.setSRID(4326);
        startLocation.setSRID(4326);
        endLocation.setSRID(4326);

        this.name = createWalkway.name();
        this.distance = createWalkway.distance();
        this.time = createWalkway.time();
        this.exposeLevel = createWalkway.exposeLevel();
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.memo = createWalkway.memo();
        this.course = course;
        this.courseImageUrl = createWalkway.courseImageUrl();
        this.hashtags = createWalkway.hashtags();

        // 연관관계 매핑
        this.member = member;

        // 생성 시 default 값
        this.likeCount = 0;
        this.reviewCount = 0;
        this.rating = 0.0;
    }

    public Walkway toWalkway() {
        CourseInfo courseInfo = new CourseInfo(distance, time, startLocation, endLocation, course, courseImageUrl);
        Author author = new Author(member.getId());
        Stat stat = new Stat(likeCount, reviewCount, rating);
        return new Walkway(id, name, getCreatedAt(), memo, stat, hashtags, courseInfo, author, exposeLevel);
    }

    public ReviewedWalkway toReviewedWalkway() {
        return new ReviewedWalkway(id, name);
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

    public ExposeLevel getExposeLevel() {
        return exposeLevel;
    }
}
