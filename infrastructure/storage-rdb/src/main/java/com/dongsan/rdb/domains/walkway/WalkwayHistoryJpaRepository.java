package com.dongsan.rdb.domains.walkway;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayHistoryJpaRepository extends JpaRepository<WalkwayHistoryEntity, Long> {
    void deleteAllInBatchByWalkwayId(Long walkwayId);
}
