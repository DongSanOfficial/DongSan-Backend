package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import jakarta.persistence.*;

@Entity
@Table(name = "walkway_log")
public class WalkwayLog extends BaseEntity {
    private static final int MIN_TIME_SEC = 0;
    private static final double MIN_DISTANCE_KM = 0.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long walkwayId;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private Double distance;

    protected WalkwayLog() {
    }

    public WalkwayLog(Long memberId, Long walkwayId, Integer time, Double distance) {
        validateTime(time);
        validateDistance(distance);

        this.memberId = memberId;
        this.walkwayId = walkwayId;
        this.time = time;
        this.distance = distance;
    }

    private void validateTime(Integer time) {
        if (time == null || time < MIN_TIME_SEC) {
            throw new CoreException(CoreErrorCode.WALKWAY_HISTORY_TIME_NOT_ENOUGH);
        }
    }

    private void validateDistance(Double distance) {
        if (distance == null || distance < MIN_DISTANCE_KM) {
            throw new CoreException(CoreErrorCode.WALKWAY_HISTORY_DISTANCE_NOT_ENOUGH);
        }
    }

    public void validateRelation(Long memberId, Long walkwayId) {
        if (!this.walkwayId.equals(walkwayId) || !this.memberId.equals(memberId)) {
            throw new CoreException(CoreErrorCode.INVALID_OWNER_AND_WALKWAY);
        }
    }

    public boolean isSufficientDistance(Double walkwayDistance) {
        return this.distance >= walkwayDistance * 2 / 3;
    }

    public void validateSufficientDistance(Double walkwayDistance) {
        if (walkwayDistance * 2 / 3 > this.distance) {
            throw new CoreException(CoreErrorCode.NOT_ENOUGH_DISTANCE);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getWalkwayId() {
        return walkwayId;
    }

    public Integer getTime() {
        return time;
    }

    public Double getDistance() {
        return distance;
    }
}
