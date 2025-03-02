package com.dongsan.core.domains.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {
    List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId);
    List<Review> getWalkwayReviewsLatest(Integer size, Long walkwayId, LocalDateTime lastCreatedAt);
    List<Review> getWalkwayReviewsRating(Integer size, Long walkwayId, LocalDateTime lastCreatedAt, Integer lastRating);
    Map<Integer, Long> getWalkwayRating(Long walkwayId);
    Optional<Review> findById(Long reviewId);
    boolean existsById(Long reviewId);
    boolean existsByIdAndMemberId(Long reviewId, Long memberId);
    Long save(CreateReview createReview);
}
