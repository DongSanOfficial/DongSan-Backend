package com.dongsan.rdb.domains.review.factory;

import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.infrastructure.ReviewRepository;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.rdb.support.util.CursorPage;
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
