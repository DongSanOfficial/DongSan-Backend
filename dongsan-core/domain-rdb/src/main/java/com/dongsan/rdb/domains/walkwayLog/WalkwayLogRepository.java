package com.dongsan.rdb.domains.walkwayLog;

import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WalkwayLogRepository {
    Optional<WalkwayLog> findById(Long walkwayHistoryId);

    Long save(WalkwayLog walkwayLog);

    CursorPage<WalkwayLog> getUserWalkwayLog(Long memberId, LocalDateTime lastCreatedAt, int size);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

}
