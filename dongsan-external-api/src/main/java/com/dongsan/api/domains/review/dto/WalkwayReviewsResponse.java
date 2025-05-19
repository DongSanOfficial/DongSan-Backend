package com.dongsan.api.domains.review.dto;

import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.rdb.support.format.TimeFormat;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record WalkwayReviewsResponse(
        Long reviewId,
        String nickname,
        String date,
        String period,
        Integer rating,
        String content
) {
    public WalkwayReviewsResponse(ReviewWithMemberQuery query) {
        this(
                query.reviewId(),
                query.nickname(),
                query.createdAt()
                        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                TimeFormat.formatTimeString(query.createdAt()),
                query.rating(),
                query.content()
        );
    }

    public static List<WalkwayReviewsResponse> from(List<ReviewWithMemberQuery> reviews) {
        return reviews.stream()
                .map(WalkwayReviewsResponse::new)
                .toList();
    }
}
