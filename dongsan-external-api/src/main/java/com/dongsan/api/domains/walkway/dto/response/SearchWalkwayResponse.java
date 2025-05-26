package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.walkway.WalkwayCoordinate;
import com.dongsan.domain.domains.walkway.domain.WalkwaySnapshot;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record SearchWalkwayResponse(
        Long walkwayId,
        String name,
        Double distance,
        List<String> hashtags,
        boolean isLike,
        Long likeCount,
        Integer reviewCount,
        Double rating,
        String courseImageUrl,
        WalkwayCoordinate location,
        String registerDate
) {

    public SearchWalkwayResponse(WalkwaySnapshot walkway, ReviewStatistic stat, Long likeCount, boolean isLiked) {
        this(
                walkway.id(),
                walkway.name(),
                walkway.distance(),
                walkway.hashtags()
                        .stream()
                        .map(hashtag -> "#" + hashtag)
                        .toList(),
                isLiked,
                likeCount,
                stat.reviewCount(),
                stat.rating(),
                walkway.courseImageUrl(),
                new WalkwayCoordinate(walkway.startLocation().getY(), walkway.startLocation().getX()),
                walkway.createdAt()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        );
    }

    public static List<SearchWalkwayResponse> from(List<WalkwaySnapshot> walkways, Map<Long, ReviewStatistic> reviewStatMap,
                                                   Map<Long, Long> likeCountMap, Set<Long> likedWalkwaySet) {
        return walkways.stream()
                .map(walkway -> {
                    Long walkwayId = walkway.id();
                    ReviewStatistic stat = reviewStatMap.get(walkwayId);
                    Long likeCount = likeCountMap.getOrDefault(walkwayId, 0L);
                    boolean isLiked = likedWalkwaySet.contains(walkwayId);

                    return new SearchWalkwayResponse(walkway, stat, likeCount, isLiked);
                }).toList();
    }
}
