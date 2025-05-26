package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "meta_walkway_rating")
public class MetaWalkwayRating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walkwayId;

    private Double rating;

    public Long getId() {
        return id;
    }

    public Double getRating() {
        return rating;
    }
}
