package com.dongsan.domain.domains.review.factory;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.domains.review.domain.ReviewRepository;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public CursorPage<ReviewWithMemberQuery> search(Long walkwayId, Review review, int size) {
        LocalDateTime lastCreatedAt = null;
        if (review != null) {
            lastCreatedAt = review.getCreatedAt();
        }
        return reviewRepository.getWalkwayReviewsLatest(walkwayId, lastCreatedAt, size);
    }
}
