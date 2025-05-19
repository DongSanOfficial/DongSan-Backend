package com.dongsan.rdb.domains.review.service;

import com.dongsan.rdb.domains.review.GetReviewsFactory;
import com.dongsan.rdb.domains.review.ReviewSort;
import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.infrastructure.ReviewRepository;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        return reviewRepository.getUserReviews(size, lastCreatedAt, memberId);
    }

    public boolean existsByReviewId(Long reviewId) {
        return reviewRepository.existsById(reviewId);
    }

    public Map<Rating, Long> getWalkwaysRating(Long walkwayId) {
        return reviewRepository.getWalkwayRating(walkwayId);
    }

    public List<Review> getWalkwayReviews(Integer size, Review review, Long walkwayId, ReviewSort sort) {
        return getReviewsServiceFactory.getService(sort)
                .search(size, review, walkwayId);
    }

    public void isReviewOwner(Long reviewId, Long memberId) {
        boolean result = reviewRepository.existsByIdAndMemberId(reviewId, memberId);
        if (!result) {
            throw new CoreException(CoreErrorCode.NOT_REVIEW_OWNER);
        }
    }

    @Transactional(readOnly = true)
    public PagingResponse<Review> getReviews(CursorRequest cursorRequest, Long memberId) {
        LocalDateTime lastCreatedAt = null;
        if (cursorRequest.lastId() != null) {
            // reviewId 검증
            // 1. 존재하는 reviewId 인지
            Review review = reviewReader.getReview(cursorRequest.lastId());
            // 2. 내가 작성한 review 인지 아닌지
            reviewValidator.isReviewOwner(review.reviewId(), memberId);
            lastCreatedAt = review.createdAt();
        }
        List<Review> reviews = reviewReader.getUserReviews(cursorRequest.size() + 1, lastCreatedAt, memberId);

        return PagingResponse.from(reviews, cursorRequest.size());
    }


}
