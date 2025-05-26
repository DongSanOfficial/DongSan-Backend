package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "walkway")
public class Walkway extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @Embedded
    private WalkwayInfo walkwayInfo;

    @Embedded
    private WalkwayGeometry geometry;

    protected Walkway() {
    }

    public Walkway(Long memberId, WalkwayInfo walkwayInfo, WalkwayGeometry geometry) {
        this.memberId = memberId;
        this.walkwayInfo = walkwayInfo;
        this.geometry = geometry;
    }

    public void updateWalkway(String name, String memo, ExposeLevel exposeLevel, List<String> hashtags) {
        walkwayInfo.updateInfo(name, memo, exposeLevel, hashtags);
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
        walkwayInfo.validateExposeLevel();
    }

    public void isOwner(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new CoreException(CoreErrorCode.NOT_WALKWAY_OWNER);
        }
    }

    public Double getDistance() {
        return this.walkwayInfo.getDistance();
    }

    public WalkwaySnapshot snapshot() {
        return new WalkwaySnapshot(
                id,
                memberId,
                walkwayInfo.getName(),
                walkwayInfo.getMemo(),
                walkwayInfo.getExposeLevel(),
                walkwayInfo.getHashtags(),
                walkwayInfo.getDistance(),
                walkwayInfo.getTime(),
                geometry.getCourse(),
                geometry.getCourseImageUrl(),
                getCreatedAt(),
                geometry.getStartLocation(),
                geometry.getEndLocation()
        );
    }

    public Long getId() {
        return id;
    }
}
