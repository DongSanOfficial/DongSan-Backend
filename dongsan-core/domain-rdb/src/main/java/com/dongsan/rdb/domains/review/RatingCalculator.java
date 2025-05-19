package com.dongsan.rdb.domains.review;

import com.dongsan.rdb.domains.review.domain.Rating;

import java.util.Map;

public class RatingCalculator {
    private RatingCalculator() {
    }

    public static Double calculateAverageRating(Map<Rating, Long> ratingCounts) {
        Double totalRating = calculateTotalRating(ratingCounts);
        Integer totalReviewCount = calculateTotalReviewCount(ratingCounts);

        if (totalReviewCount > 0) {
            return Math.round(totalRating / totalReviewCount * 10.0) / 10.0;
        }
        return 0.0;
    }

    public static Double calculateTotalRating(Map<Rating, Long> ratingCounts) {
        return ratingCounts.entrySet()
                .stream()
                .mapToDouble(entry -> entry.getKey()
                        .getNum() * entry.getValue())
                .sum();
    }

    public static Integer calculateTotalReviewCount(Map<Rating, Long> ratingCounts) {
        return (int) ratingCounts.values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}
