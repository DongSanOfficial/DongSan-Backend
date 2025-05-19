package com.dongsan.rdb.domains.review;

import com.dongsan.rdb.domains.review.domain.Rating;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.infrastructure.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        Rating rating = null;
        if (review != null) {
            lastCreatedAt = review.createdAt();
            rating = review.rating();
        }
        return reviewRepository.getWalkwayReviewsRating(size, walkwayId, lastCreatedAt, rating);
    }
}
