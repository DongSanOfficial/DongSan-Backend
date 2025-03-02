package com.dongsan.core.domains.review;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetRatingReviews implements GetReviews {
    private final ReviewRepository reviewRepository;

    @Autowired
    public GetRatingReviews(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewSort getSortType() {
        return ReviewSort.RATING;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> search(Integer size, Review review, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        Integer rating = null;
        if (review != null) {
            lastCreatedAt = review.createdAt();
            rating = review.rating();
        }
        return reviewRepository.getWalkwayReviewsRating(size, walkwayId, lastCreatedAt, rating);
    }
}
