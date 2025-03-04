package com.dongsan.core.domains.review;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewReader {
    private final ReviewRepository reviewRepository;
    private final GetReviewsFactory getReviewsServiceFactory;

    @Autowired
    public ReviewReader(ReviewRepository reviewRepository, GetReviewsFactory getReviewsServiceFactory) {
        this.reviewRepository = reviewRepository;
        this.getReviewsServiceFactory = getReviewsServiceFactory;
    }

    public Review getReview(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.REVIEW_NOT_FOUND));
    }

    public List<Review> getUserReviews(Integer size, LocalDateTime lastCreatedAt, Long memberId) {
        return reviewRepository.getUserReviews(size, lastCreatedAt, memberId);
    }

    public boolean existsByReviewId(Long reviewId){
        return reviewRepository.existsById(reviewId);
    }

    public Map<Rating, Long> getWalkwaysRating(Long walkwayId) {
        return reviewRepository.getWalkwayRating(walkwayId);
    }

    public List<Review> getWalkwayReviews(Integer size, Review review, Long walkwayId, ReviewSort sort) {
        return getReviewsServiceFactory.getService(sort).search(size, review, walkwayId);
    }
}
