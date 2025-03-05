package com.dongsan.api.domains.review;

import com.dongsan.core.domains.review.Review;
import java.util.List;

public record MyReviewResponse(
        Long reviewId,
        Long walkwayId,
        String walkwayName,
        String date,
        Integer rating,
        String content
) {
    public MyReviewResponse(Review review){
        this(
                review.reviewId(),
                review.reviewedWalkway().walkwayId(),
                review.reviewedWalkway().walkwayName(),
                review.createdAt().toString(),
                review.rating().getNum(),
                review.content()
        );
    }

    public static List<MyReviewResponse> from(List<Review> reviews){
        return reviews.stream().map(MyReviewResponse::new).toList();
    }

}
