package com.dongsan.core.domains.review;

import java.util.List;

public interface GetReviews {
    ReviewSort getSortType();

    List<Review> search(Integer size, Review review, Long walkwayId);
}
