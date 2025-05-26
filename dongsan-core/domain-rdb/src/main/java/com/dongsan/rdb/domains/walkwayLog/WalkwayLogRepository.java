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

    boolean isReviewed(Long walkwayLogId);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

//    List<WalkwayLog> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
//                                                LocalDateTime lastCreatedAt);

//    List<WalkwayLog> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt);

//    void updateWalkwayHistoryIsReviewed(Long walkwayHistoryId, boolean isReviewed);

}
