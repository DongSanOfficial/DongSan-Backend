package com.dongsan.rdb.domains.review.service;

import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.domain.ReviewStatistic;
import com.dongsan.rdb.domains.review.factory.GetReviewsFactory;
import com.dongsan.rdb.domains.review.factory.ReviewSort;
import com.dongsan.rdb.domains.review.infrastructure.ReviewRepository;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithWalkwayQuery;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import com.dongsan.rdb.support.util.CursorPage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ReviewRdbService {
    private final ReviewRepository reviewRepository;
    private final GetReviewsFactory getReviewsServiceFactory;

    public ReviewRdbService(ReviewRepository reviewRepository, GetReviewsFactory getReviewsServiceFactory) {
        this.reviewRepository = reviewRepository;
        this.getReviewsServiceFactory = getReviewsServiceFactory;
    }

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.REVIEW_NOT_FOUND));
    }

    public Long save(Long walkwayLogId, int rating, String content) {
        Review review = new Review(walkwayLogId, rating, content);
        return reviewRepository.save(review);
    }

    public LocalDateTime getReviewCreatedAt(Long reviewId) {
        if (reviewId == null) {
            return null;
        }
        return getReview(reviewId).getCreatedAt();
    }

    public CursorPage<ReviewWithMemberQuery> getWalkwayReviews(Long walkwayId, ReviewSort sort, Review review, int size) {
        return getReviewsServiceFactory.getService(sort)
                .search(walkwayId, review, size);
    }

    public CursorPage<ReviewWithWalkwayQuery> getUserReviews(Long memberId, LocalDateTime lastCreatedAt, int size) {
        return reviewRepository.getUserReviews(memberId, lastCreatedAt, size);
    }

    public void deleteAllInBatchByWalkwayId(Long walkwayId) {
        reviewRepository.deleteAllInBatchByWalkwayId(walkwayId);
    }

    public ReviewStatistic getReviewStat(Long walkwayId) {
        return reviewRepository.getReviewStat(walkwayId);
    }

    // {walkwayId, ReviewStat}
    public Map<Long, ReviewStatistic> getReviewStats(List<Long> walkwayIds) {
        return reviewRepository.getReviewStats(walkwayIds);
    }

    // {walkwayLogId, Review}
    public Map<Long, Review> getReviews(List<Long> walkwayLogIds) {
        return reviewRepository.getReviews(walkwayLogIds);
    }
}
