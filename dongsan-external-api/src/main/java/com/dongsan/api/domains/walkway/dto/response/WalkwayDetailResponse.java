package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.walkway.LineStringMapper;
import com.dongsan.domain.domains.walkway.WalkwayCoordinate;
import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;
import com.dongsan.domain.domains.walkway.domain.WalkwaySnapshot;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record WalkwayDetailResponse(
        String date,
        Integer time,
        Double distance,
        String name,
        String memo,
        double rating,
        boolean isLiked,
        int reviewCount,
        int likeCount,
        List<String> hashtags,
        WalkwayExposeLevel accessLevel,
        List<WalkwayCoordinate> course,
        boolean marked
) {

    public WalkwayDetailResponse(WalkwaySnapshot walkway, boolean isLike, boolean isMarked, ReviewStatistic reviewStatistic, int likeCount) {
        this(
                walkway.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                walkway.time(),
                walkway.distance(),
                walkway.name(),
                walkway.memo(),
                reviewStatistic.rating(),
                isLike,
                reviewStatistic.reviewCount(),
                likeCount,
                walkway.hashtags()
                        .stream()
                        .map(hashtag -> "#" + hashtag)
                        .toList(),
                walkway.walkwayExposeLevel(),
                LineStringMapper.toList(walkway.course()),
                isMarked
        );
    }

}
