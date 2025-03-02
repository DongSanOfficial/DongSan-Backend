package com.dongsan.api.domains.review;

import com.dongsan.core.domains.review.Review;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.util.List;

public record GetReviewResponse(
        List<ReviewInfo> reviews,
        boolean hasNext
) {
    public static GetReviewResponse from(CursorPagingResponse<Review> cursorPagingResponse){
        return new GetReviewResponse(cursorPagingResponse.data().stream().map(ReviewInfo::new).toList(), cursorPagingResponse.hasNext());
    }

    public record ReviewInfo(
            Long reviewId,
            Long walkwayId,
            String walkwayName,
            String date,
            Integer rating,
            String content
    ){
        public ReviewInfo(Review review){
            this(
                    review.reviewId(),
                    review.reviewedWalkway().walkwayId(),
                    review.reviewedWalkway().walkwayName(),
                    review.createdAt().toString(),
                    review.rating(),
                    review.content()
            );
        }
    }
}
