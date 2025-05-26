package com.dongsan.rdb.domains.review.domain;

import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithWalkwayQuery;
import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ReviewRepository {
    Long save(Review review);

    Optional<Review> findById(Long reviewId);

    CursorPage<ReviewWithWalkwayQuery> getUserReviews(Long memberId, LocalDateTime lastCreatedAt, int size);

    CursorPage<ReviewWithMemberQuery> getWalkwayReviewsLatest(Long walkwayId, LocalDateTime lastCreatedAt, int size);

    CursorPage<ReviewWithMemberQuery> getWalkwayReviewsRating(Long walkwayId, Integer lastRating, LocalDateTime lastCreatedAt, int size);

    ReviewStatistic getReviewStat(Long walkwayId);

    Map<Long, ReviewStatistic> getReviewStats(List<Long> walkwayIds);

    void deleteAllInBatchByWalkwayId(Long walkwayId);

    Map<Long, Review> getReviews(List<Long> walkwayLogIds);

    boolean isReviewed(Long walkwayLogId);
}
