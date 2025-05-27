package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwaySnapshot;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record WalkwayLogWithReviewResponse(
        // 산책로
        Long walkwayId,
        String name,
        LocalDate date,
        Double distance,
        String courseImageUrl,
        Integer time,
        String memo,
        Long likeCount,
        Integer reviewCount,
        Double rating,
        List<String> hashtags,
        // 산책 log
        Long walkwayHistoryId,
        LocalDateTime walkwayHistoryDate,
        Integer walkwayHistoryTime,
        Double walkwayHistoryDistance,
        // 리뷰 작성 여부
        boolean hasReviewed,
        Long reviewId,
        boolean canReview  // 거리는 충분한데, 리뷰는 아직 작성하지 않음
) {
    public WalkwayLogWithReviewResponse(WalkwaySnapshot walkway, WalkwayLog walkwayLog,
                                        ReviewStatistic stat, Long likeCount,
                                        boolean hasReviewed, Long reviewId, boolean canReview) {
        this(
                walkway.id(),
                walkway.name(),
                walkway.createdAt().toLocalDate(),
                walkway.distance(),
                walkway.courseImageUrl(),
                walkway.time(),
                walkway.memo(),
                likeCount,
                stat.reviewCount(),
                stat.rating(),
                walkway.hashtags(),
                walkwayLog.getId(),
                walkwayLog.getCreatedAt(),
                walkwayLog.getTime(),
                walkwayLog.getDistance(),
                hasReviewed,
                reviewId,
                canReview
        );
    }

    /**
     * @param walkwayLogs   List<WalkwayLog>
     * @param walkwayMap    {walkwayId, Walkway}
     * @param reviewMap     {walkwayLogId, Review}
     * @param reviewStatMap
     * @param likeCountMap
     */
    public static List<WalkwayLogWithReviewResponse> from(List<WalkwayLog> walkwayLogs, Map<Long, Walkway> walkwayMap,
                                                          Map<Long, Review> reviewMap, Map<Long, ReviewStatistic> reviewStatMap,
                                                          Map<Long, Long> likeCountMap) {
        return walkwayLogs.stream().map(walkwayLog -> {
            Long walkwayLogId = walkwayLog.getId();
            Long walkwayId = walkwayLog.getWalkwayId();
            WalkwaySnapshot walkwaySnapshot = walkwayMap.get(walkwayId).snapshot();

            ReviewStatistic stat = reviewStatMap.get(walkwayId);
            Long likeCount = likeCountMap.getOrDefault(walkwayId, 0L);

            boolean hasReviewed = reviewMap.containsKey(walkwayLogId);
            Long reviewId = hasReviewed ? reviewMap.get(walkwayLogId).getId() : null;
            boolean canReview = walkwayLog.isSufficientDistance(walkwaySnapshot.distance()) && !hasReviewed;

            return new WalkwayLogWithReviewResponse(walkwaySnapshot, walkwayLog, stat, likeCount, hasReviewed, reviewId, canReview);
        }).toList();
    }
}

