package com.dongsan.api.domains.review;

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
        double totalRating = ratingCounts.entrySet().stream()
                .mapToDouble(entry -> entry.getKey() * entry.getValue())
                .sum();

        int totalReviewCount = (int) ratingCounts.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        double avgRating = 0.0;
        if (totalReviewCount > 0) {
            avgRating = Math.round(totalRating / totalReviewCount * 10.0) / 10.0;
        }

        Long fiveRate = ratingCounts.getOrDefault(5, 0L) * 100 / totalReviewCount;
        Long fourRate = ratingCounts.getOrDefault(4, 0L) * 100 / totalReviewCount;
        Long threeRate = ratingCounts.getOrDefault(3, 0L) * 100 / totalReviewCount;
        Long twoRate = ratingCounts.getOrDefault(2, 0L) * 100 / totalReviewCount;
        Long oneRate = ratingCounts.getOrDefault(1, 0L) * 100 / totalReviewCount;

        return new GetWalkwayRatingResponse(avgRating, totalReviewCount, fiveRate, fourRate, threeRate, twoRate, oneRate);
    }
}
