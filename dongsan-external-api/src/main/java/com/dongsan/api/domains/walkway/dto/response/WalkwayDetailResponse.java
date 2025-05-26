package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.rdb.domains.review.domain.ReviewStatistic;
import com.dongsan.rdb.domains.walkway.LineStringMapper;
import com.dongsan.rdb.domains.walkway.WalkwayCoordinate;
import com.dongsan.rdb.domains.walkway.domain.ExposeLevel;
import com.dongsan.rdb.domains.walkway.domain.WalkwaySnapshot;

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
        ExposeLevel accessLevel,
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
                walkway.exposeLevel(),
                LineStringMapper.toList(walkway.course()),
                isMarked
        );
    }

}
