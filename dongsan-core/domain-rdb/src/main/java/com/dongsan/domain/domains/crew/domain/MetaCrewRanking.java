package com.dongsan.domain.domains.crew.domain;

import com.dongsan.domain.domains.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "meta_crew_ranking")
public class MetaCrewRanking extends BaseEntity {

    @Id
    private Long crewId;

    @Column(name = "log_count", nullable = false)
    private Long logCount;

    protected MetaCrewRanking() {
    }

    public MetaCrewRanking(Long crewId) {
        this.crewId = crewId;
        this.logCount = 0L;
    }

    public Long getCrewId() {
        return crewId;
    }

    public Long getLogCount() {
        return logCount;
    }

    public void addLogCount(Long logCount) {
        this.logCount += logCount;
    }
}
