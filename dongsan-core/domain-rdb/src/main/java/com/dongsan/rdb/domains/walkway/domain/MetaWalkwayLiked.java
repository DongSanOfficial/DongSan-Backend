package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "meta_walkway_liked")
public class MetaWalkwayLiked extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walkwayId;

    private Integer likeCount;

    public Integer getLikeCount() {
        return likeCount;
    }

    public Long getId() {
        return id;
    }
}
