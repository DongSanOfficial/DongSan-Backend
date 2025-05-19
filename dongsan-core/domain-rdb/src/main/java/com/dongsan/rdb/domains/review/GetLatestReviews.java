package com.dongsan.rdb.domains.review;

import com.dongsan.rdb.domains.review.infrastructure.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GetLatestReviews implements GetReviews {
    private final ReviewRepository reviewRepository;

    @Autowired
    public GetLatestReviews(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ReviewSort getSortType() {
        return ReviewSort.LATEST;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> search(Integer size, Review review, Long walkwayId) {
        LocalDateTime lastCreatedAt = null;
        if (review != null) {
            lastCreatedAt = review.createdAt();
        }
        return reviewRepository.getWalkwayReviewsLatest(size, walkwayId, lastCreatedAt);
    }
}
