package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.rdb.domains.review.domain.ReviewStat;
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

    public WalkwayDetailResponse(WalkwaySnapshot walkway, boolean isLike, boolean isMarked, ReviewStat reviewStat, int likeCount) {
        this(
                walkway.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                walkway.time(),
                walkway.distance(),
                walkway.name(),
                walkway.memo(),
                reviewStat.rating(),
                isLike,
                reviewStat.reviewCount(),
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

    // TODO
    public WalkwayDetailResponse(WalkwaySnapshot walkway, boolean isLiked, boolean isMarked) {
        this(
                walkway.createdAt()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                walkway.courseInfo()
                        .time(),
                walkway.courseInfo()
                        .distance(),
                walkway.name(),
                walkway.memo(),
                walkway.stat()
                        .rating(),
                isLiked,
                walkway.stat()
                        .reviewCount(),
                walkway.stat()
                        .likeCount(),
                walkway.hashtags()
                        .stream()
                        .map(hashtag -> "#" + hashtag)
                        .toList(),
                walkway.exposeLevel(),
                LineStringMapper.toList(walkway.courseInfo()
                        .course()),
                isMarked
        );
    }


}
