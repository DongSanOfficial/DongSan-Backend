package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.rdb.domains.review.domain.ReviewStatistic;
import com.dongsan.rdb.domains.walkway.domain.WalkwaySnapshot;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record WalkwaySimpleResponse(
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
        List<String> hashtags
) {
    public WalkwaySimpleResponse(WalkwaySnapshot walkway, ReviewStatistic reviewStatistic, Long likeCount) {
        this(
                walkway.id(),
                walkway.name(),
                walkway.createdAt().toLocalDate(),
                walkway.distance(),
                walkway.courseImageUrl(),
                walkway.time(),
                walkway.memo(),
                likeCount,
                reviewStatistic.reviewCount(),
                reviewStatistic.rating(),
                walkway.hashtags()
        );
    }

    public static List<WalkwaySimpleResponse> from(List<WalkwaySnapshot> walkways, Map<Long, ReviewStatistic> reviewStatMap, Map<Long, Long> likeCountMap) {
        return walkways.stream()
                .map(walkway -> {
                    Long walkwayId = walkway.id();
                    ReviewStatistic stat = reviewStatMap.get(walkwayId);
                    Long likeCount = likeCountMap.getOrDefault(walkwayId, 0L);

                    return new WalkwaySimpleResponse(walkway, stat, likeCount);
                })
                .toList();
    }
}
