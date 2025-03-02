package com.dongsan.api.domains.review;

import com.dongsan.core.domains.review.Review;
import com.dongsan.core.support.format.TimeFormat;
import com.dongsan.core.support.util.CursorPagingResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record GetWalkwayReviewsResponse(
        List<WalkwayReview> reviews,
        Boolean hasNext
) {

    public GetWalkwayReviewsResponse(CursorPagingResponse<Review> cursorPagingResponse) {
        this(
                cursorPagingResponse.data().stream()
                        .map(review -> new WalkwayReview(
                                        review.reviewId(),
                                        review.reviewer().nickname(),
                                        review.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                                        TimeFormat.formatTimeString(review.createdAt()),
                                        review.rating(),
                                        review.content()
                                )
                        )
                        .toList(),
                cursorPagingResponse.hasNext()
        );
    }

    public record WalkwayReview(
            Long reviewId,
            String nickname,
            String date,
            String period,
            Integer rating,
            String content
    ) {
    }
}
