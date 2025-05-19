package com.dongsan.rdb.domains.review.factory;

import com.dongsan.rdb.common.CursorPage;
import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;

public interface GetReviews {
    ReviewSort getSortType();

    CursorPage<ReviewWithMemberQuery> search(Long walkwayId, Review review, int size);
}
