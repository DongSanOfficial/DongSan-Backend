package com.dongsan.rdb.domains.review.domain;

import com.dongsan.rdb.domains.review.RatingCalculator;

import java.util.Map;

public record ReviewStatistic(
        double rating,
        int reviewCount,
        long five,
        long four,
        long three,
        long two,
        long one
) {
    public static ReviewStatistic from(Map<Rating, Long> countPerRating) {
        int totalReviewCount = RatingCalculator.calculateTotalReviewCount(countPerRating);
        double avgRating = totalReviewCount > 0
                ? Math.round(RatingCalculator.calculateAverageRating(countPerRating) * 10.0) / 10.0
                : 0.0;

        long fiveRate =
                totalReviewCount == 0.0 ? 0L : countPerRating.getOrDefault(Rating.FIVE, 0L) * 100 / totalReviewCount;
        long fourRate =
                totalReviewCount == 0.0 ? 0L : countPerRating.getOrDefault(Rating.FOUR, 0L) * 100 / totalReviewCount;
        long threeRate =
                totalReviewCount == 0.0 ? 0L : countPerRating.getOrDefault(Rating.THREE, 0L) * 100 / totalReviewCount;
        long twoRate =
                totalReviewCount == 0.0 ? 0L : countPerRating.getOrDefault(Rating.TWO, 0L) * 100 / totalReviewCount;
        long oneRate =
                totalReviewCount == 0.0 ? 0L : countPerRating.getOrDefault(Rating.ONE, 0L) * 100 / totalReviewCount;

        return new ReviewStatistic(avgRating, totalReviewCount, fiveRate, fourRate, threeRate, twoRate, oneRate);
    }
}
