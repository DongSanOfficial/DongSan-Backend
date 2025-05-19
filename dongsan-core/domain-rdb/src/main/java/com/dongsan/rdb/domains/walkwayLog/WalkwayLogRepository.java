package com.dongsan.rdb.domains.walkwayLog;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalkwayLogRepository {
    Optional<WalkwayLog> findById(Long walkwayHistoryId);

    Long saveWalkwayHistory(CreateWalkwayHistory createWalkwayHistory);

    List<WalkwayLog> getCanReviewWalkwayHistory(Long walkwayId, Long memberId, int size,
                                                LocalDateTime lastCreatedAt);

    List<WalkwayLog> getUserCanReviewWalkwayHistory(Long memberId, int size, LocalDateTime lastCreatedAt);


    void updateWalkwayHistoryIsReviewed(Long walkwayHistoryId, boolean isReviewed);

    boolean isReviewed(Long walkwayLogId);
}
