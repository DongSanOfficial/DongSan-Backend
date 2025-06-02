package com.dongsan.domain.domains.review.factory;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.domain.ReviewRepository;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public CursorResponse<ReviewWithMemberQuery> search(Long walkwayId, Review review, int size) {
        LocalDateTime lastCreatedAt = null;
        Integer rating = null;
        if (review != null) {
            lastCreatedAt = review.getCreatedAt();
            rating = review.getRating();
        }
        return reviewRepository.getWalkwayReviewsRating(walkwayId, rating, lastCreatedAt, size);
    }
}
