package com.dongsan.domain.domains.review.factory;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.domain.support.util.CursorResponse;

public interface GetReviews {
    ReviewSort getSortType();

    CursorResponse<ReviewWithMemberQuery> search(Long walkwayId, Review review, int size);
}
