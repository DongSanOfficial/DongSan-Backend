package com.dongsan.api.domains.bookmark;

import com.dongsan.rdb.domains.bookmark.domain.MarkedWalkway;
import com.dongsan.rdb.domains.review.domain.ReviewStatistic;
import com.dongsan.rdb.domains.walkway.domain.Walkway;
import com.dongsan.rdb.domains.walkway.domain.WalkwaySnapshot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record MarkedWalkwayResponse(
        Long walkwayId,
        String name,
        Double distance,
        List<String> hashtags,
        String courseImageUrl,
        Long likeCount,
        int reviewCount,
        double rating,
        LocalDateTime date  // 북마크 추가 시간
) {

    public MarkedWalkwayResponse(WalkwaySnapshot walkway, ReviewStatistic stat, Long likeCount, LocalDateTime createdAt) {
        this(
                walkway.id(),
                walkway.name(),
                walkway.distance(),
                walkway.hashtags(),
                walkway.courseImageUrl(),
                likeCount,
                stat.reviewCount(),
                stat.rating(),
                createdAt
        );
    }

    public static List<MarkedWalkwayResponse> from(List<MarkedWalkway> markedWalkways, Map<Long, Walkway> walkwayMap,
                                                   Map<Long, ReviewStatistic> reviewStatMap, Map<Long, Long> likeCountMap) {
        return markedWalkways.stream()
                .map(markedWalkway -> {
                    Long walkwayId = markedWalkway.getWalkwayId();
                    WalkwaySnapshot walkwaySnapshot = walkwayMap.get(walkwayId).snapshot();
                    ReviewStatistic stat = reviewStatMap.get(walkwayId);
                    Long likeCount = likeCountMap.get(walkwayId);

                    return new MarkedWalkwayResponse(walkwaySnapshot, stat, likeCount, markedWalkway.getCreatedAt());
                }).toList();
    }
}
