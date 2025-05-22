package com.dongsan.rdb.domains.review.infrastructure;

import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ReviewRepository {
    Long save(Review review);

    Optional<Review> findById(Long reviewId);

    CursorPage<ReviewWithWalkwayQuery> getUserReviews(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorPage<ReviewWithMemberQuery> getWalkwayReviewsLatest(Long walkwayId, LocalDateTime lastCreatedAt, int size);

    CursorPage<ReviewWithMemberQuery> getWalkwayReviewsRating(Long walkwayId, Integer lastRating, LocalDateTime lastCreatedAt, int size);

    Map<Rating, Long> getWalkwayRating(Long walkwayId);

}
