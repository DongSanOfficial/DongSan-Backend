package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import jakarta.persistence.*;

@Entity
@Table(name = "walkway_log")
public class WalkwayLog extends BaseEntity {
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
        this.memberId = memberId;
        this.walkwayId = walkwayId;

        this.time = time;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void validateRelation(Long memberId, Long walkwayId) {
        if (!this.walkwayId.equals(walkwayId) || !this.memberId.equals(memberId)) {
            throw new CoreException(CoreErrorCode.INVALID_ACCESS);
        }
    }

    public void validateSufficientDistance(Double walkwayDistance) {
        if (walkwayDistance * 2 / 3 > this.distance) {
            throw new CoreException(CoreErrorCode.NOT_ENOUGH_DISTANCE);
        }
    }
}
