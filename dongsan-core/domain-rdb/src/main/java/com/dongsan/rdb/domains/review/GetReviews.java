package com.dongsan.rdb.domains.review;

import com.dongsan.rdb.domains.review.domain.Review;

import java.util.List;

public interface GetReviews {
    ReviewSort getSortType();

    List<Review> search(Integer size, Review review, Long walkwayId);
}
