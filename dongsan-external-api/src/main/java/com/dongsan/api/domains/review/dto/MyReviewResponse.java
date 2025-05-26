package com.dongsan.api.domains.review.dto;

import com.dongsan.domain.domains.review.infrastructure.ReviewWithWalkwayQuery;

import java.util.List;

public record MyReviewResponse(
        Long reviewId,
        Long walkwayId,
        String walkwayName,
        String date,
        Integer rating,
        String content
) {
    public MyReviewResponse(ReviewWithWalkwayQuery query) {
        this(
                query.reviewId(),
                query.walkwayId(),
                query.walkwayName(),
                query.createdAt().toString(),
                query.rating(),
                query.content()
        );
    }

    public static List<MyReviewResponse> from(List<ReviewWithWalkwayQuery> reviews) {
        return reviews.stream()
                .map(MyReviewResponse::new)
                .toList();
    }

}
