package com.dongsan.domain.domains.walkway.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "meta_walkway_rating")
public class MetaWalkwayRating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walkwayId;

    private long ratingCount;

    private long ratingSum;

    private double rating;

    protected MetaWalkwayRating() {
    }

    // 비동기 예외 필요
    public MetaWalkwayRating(Long walkwayId) {
        this.walkwayId = walkwayId;
        this.ratingCount = 0;
        this.ratingSum = 0;
        this.rating = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void addRating(int rating) {
        if (rating < 0 || rating > 5) {
            return;
        }
        this.ratingSum += rating;
        this.ratingCount += 1;
        this.rating = (double) this.ratingSum / this.ratingCount;
    }

    public void removeRating(int rating) {
        if (rating < 0 || rating > 5) {
            return;
        }
        this.ratingSum -= rating;
        this.ratingCount -= Math.max(0, this.ratingCount - 1);
        this.rating = this.ratingCount == 0 ? 0.0 : (double) this.ratingSum / this.ratingCount;
    }

    public double getRating() {
        if (ratingCount == 0) {
            return 0.0;
        }
        return (double) ratingSum / ratingCount;
    }
}
