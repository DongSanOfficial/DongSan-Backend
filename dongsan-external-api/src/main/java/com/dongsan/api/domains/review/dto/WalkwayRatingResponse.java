package com.dongsan.api.domains.review.dto;

import com.dongsan.domain.domains.review.RatingCalculator;
import com.dongsan.domain.domains.review.domain.Rating;

import java.util.Map;

public record WalkwayRatingResponse(
        Double rating,
        Integer reviewCount,
        Long five,
        Long four,
        Long three,
        Long two,
        Long one
) {
    public static WalkwayRatingResponse from(Map<Rating, Long> ratingCounts) {

        Integer totalReviewCount = RatingCalculator.calculateTotalReviewCount(ratingCounts);
        Double avgRating = totalReviewCount > 0
                ? Math.round(RatingCalculator.calculateAverageRating(ratingCounts) * 10.0) / 10.0
                : 0.0;

        Long fiveRate =
                totalReviewCount == 0.0 ? 0L : ratingCounts.getOrDefault(Rating.FIVE, 0L) * 100 / totalReviewCount;
        Long fourRate =
                totalReviewCount == 0.0 ? 0L : ratingCounts.getOrDefault(Rating.FOUR, 0L) * 100 / totalReviewCount;
        Long threeRate =
                totalReviewCount == 0.0 ? 0L : ratingCounts.getOrDefault(Rating.THREE, 0L) * 100 / totalReviewCount;
        Long twoRate =
                totalReviewCount == 0.0 ? 0L : ratingCounts.getOrDefault(Rating.TWO, 0L) * 100 / totalReviewCount;
        Long oneRate =
                totalReviewCount == 0.0 ? 0L : ratingCounts.getOrDefault(Rating.ONE, 0L) * 100 / totalReviewCount;

        return new WalkwayRatingResponse(avgRating, totalReviewCount, fiveRate, fourRate, threeRate, twoRate, oneRate);
    }
}
