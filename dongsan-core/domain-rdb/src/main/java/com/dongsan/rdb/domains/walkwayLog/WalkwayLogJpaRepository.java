package com.dongsan.rdb.domains.walkwayLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkwayLogJpaRepository extends JpaRepository<WalkwayLog, Long> {
    void deleteAllInBatchByWalkwayId(Long walkwayId);

}
