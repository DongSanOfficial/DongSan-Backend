package com.dongsan.rdb.domains.review.service;

import com.dongsan.rdb.common.CursorPage;
import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.factory.GetReviewsFactory;
import com.dongsan.rdb.domains.review.factory.ReviewSort;
import com.dongsan.rdb.domains.review.infrastructure.ReviewRepository;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithWalkwayQuery;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional(readOnly = true)
    public CursorPage<ReviewWithMemberQuery> getWalkwayReviews(Long walkwayId, ReviewSort sort, Review review, int size) {
        return getReviewsServiceFactory.getService(sort)
                .search(walkwayId, review, size);
    }

    @Transactional(readOnly = true)
    public Map<Rating, Long> getWalkwayRating(Long walkwayId) {
        return reviewRepository.getWalkwayRating(walkwayId);
    }

    @Transactional(readOnly = true)
    public CursorPage<ReviewWithWalkwayQuery> getUserReviews(Long memberId, LocalDateTime lastCreatedAt, int size) {
        return reviewRepository.getUserReviews(memberId, lastCreatedAt, size);
    }

}
