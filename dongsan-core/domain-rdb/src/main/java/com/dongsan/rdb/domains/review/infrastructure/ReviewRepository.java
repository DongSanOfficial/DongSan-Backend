package com.dongsan.rdb.domains.review.infrastructure;

import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ReviewRepository {
    Long save(Review review);

    List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId);

    List<Review> getWalkwayReviewsLatest(Integer size, Long walkwayId, LocalDateTime lastCreatedAt);

    List<Review> getWalkwayReviewsRating(Integer size, Long walkwayId, LocalDateTime lastCreatedAt, Rating lastRating);

    Map<Rating, Long> getWalkwayRating(Long walkwayId);

    Optional<Review> findById(Long reviewId);

    boolean existsById(Long reviewId);

    boolean existsByIdAndMemberId(Long reviewId, Long memberId);


}
