package com.dongsan.domain.domains.bookmark.domain;

import com.dongsan.domain.domains.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "marked_walkway")
public class MarkedWalkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookmarkId;

    private Long walkwayId;

    protected MarkedWalkway() {
    }

    public MarkedWalkway(Long bookmarkId, Long walkwayId) {
        this.bookmarkId = bookmarkId;
        this.walkwayId = walkwayId;
    }

    public Long getWalkwayId() {
        return walkwayId;
    }
}
