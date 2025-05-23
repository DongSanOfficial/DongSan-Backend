package com.dongsan.rdb.domains.review;

import com.dongsan.rdb.domains.review.domain.Rating;

import java.util.Map;

public class RatingCalculator {
    private RatingCalculator() {
    }

    public static double calculateAverageRating(Map<Rating, Long> ratingCounts) {
        double totalRating = calculateTotalRating(ratingCounts);
        int totalReviewCount = calculateTotalReviewCount(ratingCounts);

        if (totalReviewCount > 0) {
            return Math.round(totalRating / totalReviewCount * 10.0) / 10.0;
        }
        return 0.0;
    }

    public static double calculateTotalRating(Map<Rating, Long> ratingCounts) {
        return ratingCounts.entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey()
                        .getNum() * entry.getValue())
                .sum();
    }

    public static int calculateTotalReviewCount(Map<Rating, Long> ratingCounts) {
        return (int) ratingCounts.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}
