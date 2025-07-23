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

    public double getRating() {
        if (ratingCount == 0) {
            return 0.0;
        }
        return (double) ratingSum / ratingCount;
    }
}
