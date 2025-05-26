package com.dongsan.api.domains.review;

import com.dongsan.domain.domains.review.infrastructure.ReviewWithWalkwayQuery;
import com.dongsan.domain.domains.review.service.ReviewRdbService;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.support.util.CursorRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class UserReviewFacade {
    private final ReviewRdbService reviewRdbService;

    public UserReviewFacade(ReviewRdbService reviewRdbService) {
        this.reviewRdbService = reviewRdbService;
    }

    @Transactional(readOnly = true)
    public CursorPage<ReviewWithWalkwayQuery> getUserReviews(CursorRequest paging, Long memberId) {
        LocalDateTime lastCreatedAt = reviewRdbService.getReviewCreatedAt(paging.lastId());
        return reviewRdbService.getUserReviews(memberId, lastCreatedAt, paging.size());
    }
}
