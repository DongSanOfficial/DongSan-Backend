package com.dongsan.api.domains.review;

import com.dongsan.core.domains.review.RatingCalculator;
import java.util.Map;

public record GetWalkwayRatingResponse(
        Double rating,
        Integer reviewCount,
        Long five,
        Long four,
        Long three,
        Long two,
        Long one
) {
    public static GetWalkwayRatingResponse from(Map<Integer, Long> ratingCounts) {

        Integer totalReviewCount = RatingCalculator.calculateTotalReviewCount(ratingCounts);
        Double avgRating = totalReviewCount > 0
                ? Math.round(RatingCalculator.calculateAverageRating(ratingCounts) * 10.0) / 10.0
                : 0.0;

        Long fiveRate = ratingCounts.getOrDefault(5, 0L) * 100 / totalReviewCount;
        Long fourRate = ratingCounts.getOrDefault(4, 0L) * 100 / totalReviewCount;
        Long threeRate = ratingCounts.getOrDefault(3, 0L) * 100 / totalReviewCount;
        Long twoRate = ratingCounts.getOrDefault(2, 0L) * 100 / totalReviewCount;
        Long oneRate = ratingCounts.getOrDefault(1, 0L) * 100 / totalReviewCount;

        return new GetWalkwayRatingResponse(avgRating, totalReviewCount, fiveRate, fourRate, threeRate, twoRate, oneRate);
    }
}
