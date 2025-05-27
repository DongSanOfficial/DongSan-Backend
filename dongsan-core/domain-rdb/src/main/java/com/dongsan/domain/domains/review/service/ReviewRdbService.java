package com.dongsan.domain.domains.review.service;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.domain.ReviewStatistic;
import com.dongsan.domain.domains.review.factory.GetReviewsFactory;
import com.dongsan.domain.domains.review.factory.ReviewSort;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.domains.review.domain.ReviewRepository;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithWalkwayQuery;
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

    public void validateReviewWritable(Long walkwayLogId) {
        boolean isReviewed = reviewRepository.isReviewed(walkwayLogId);
        if (isReviewed) {
            throw new CoreException(CoreErrorCode.ALREADY_REVIEWED);
        }
    }
}
