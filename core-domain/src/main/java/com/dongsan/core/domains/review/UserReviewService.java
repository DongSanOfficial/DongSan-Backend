package com.dongsan.core.domains.review;

import com.dongsan.core.support.util.CursorPagingRequest;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserReviewService {
    private final ReviewReader reviewReader;
    private final ReviewValidator reviewValidator;

    @Autowired
    public UserReviewService(ReviewReader reviewReader, ReviewValidator reviewValidator) {
        this.reviewReader = reviewReader;
        this.reviewValidator = reviewValidator;
    }

    @Transactional(readOnly = true)
    public CursorPagingResponse<Review> getReviews(CursorPagingRequest cursorPagingRequest, Long memberId) {
        LocalDateTime lastCreatedAt = null;
        if(cursorPagingRequest.lastId() != null){
            // reviewId 검증
            // 1. 존재하는 reviewId 인지
            Review review = reviewReader.getReview(cursorPagingRequest.lastId());
            // 2. 내가 작성한 review 인지 아닌지
            reviewValidator.isReviewOwner(review.reviewId(), memberId);
            lastCreatedAt = review.createdAt();
        }
        List<Review> reviews = reviewReader.getUserReviews(cursorPagingRequest.size()+1, lastCreatedAt, memberId);

        return CursorPagingResponse.from(reviews, cursorPagingRequest.size());
    }


}
