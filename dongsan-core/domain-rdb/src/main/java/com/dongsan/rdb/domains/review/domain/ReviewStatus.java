package com.dongsan.rdb.domains.review.domain;

public record ReviewStatus(
        boolean canReview,
        boolean hasReviewed,
        Long reviewId
) {
}
