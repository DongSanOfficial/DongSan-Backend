package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.domains.common.BaseEntity;
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

    // @Column(nullable = false)
    // private Boolean isReviewed;  // 이거 굳이 필요...?

    protected WalkwayLog() {
    }

    public WalkwayLog(Long memberId, Long walkwayId, Integer time, Double distance) {
        this.memberId = memberId;
        this.walkwayId = walkwayId;

        this.time = time;
        this.distance = distance;
    }
}
