package com.dongsan.domain.domains.review.factory;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.domains.review.infrastructure.ReviewWithMemberQuery;

public interface GetReviews {
    ReviewSort getSortType();

    CursorPage<ReviewWithMemberQuery> search(Long walkwayId, Review review, int size);
}
