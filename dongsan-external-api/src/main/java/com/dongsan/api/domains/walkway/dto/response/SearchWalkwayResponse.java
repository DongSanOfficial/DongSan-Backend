package com.dongsan.api.domains.walkway.dto.response;

import com.dongsan.rdb.domains.walkway.Walkway;
import com.dongsan.rdb.domains.walkway.WalkwayCoordinate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public record SearchWalkwayResponse(
        Long walkwayId,
        String name,
        Double distance,
        List<String> hashtags,
        Boolean isLike,
        Integer likeCount,
        Integer reviewCount,
        Double rating,
        String courseImageUrl,
        WalkwayCoordinate location,
        String registerDate
) {
    public SearchWalkwayResponse(Walkway walkway, Boolean isLiked) {
        this(
                walkway.walkwayId(),
                walkway.name(),
                walkway.courseInfo()
                        .distance(),
                walkway.hashtags()
                        .stream()
                        .map(hashtag -> "#" + hashtag)
                        .toList(),
                isLiked,
                walkway.stat()
                        .likeCount(),
                walkway.stat()
                        .reviewCount(),
                walkway.stat()
                        .rating(),
                walkway.courseInfo()
                        .courseImageUrl(),
                new WalkwayCoordinate(walkway.courseInfo()
                        .startLocation()
                        .getY(), walkway.courseInfo()
                        .startLocation()
                        .getX()),
                walkway.createdAt()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
        );
    }

    public static List<SearchWalkwayResponse> from(List<Walkway> walkways, Map<Long, Boolean> isLiked) {
        return walkways.stream()
                .map(walkway -> new SearchWalkwayResponse(walkway, isLiked.get(walkway.walkwayId())))
                .toList();
    }

}
