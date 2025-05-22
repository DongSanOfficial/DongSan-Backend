package com.dongsan.rdb.domains.review.factory;

import com.dongsan.rdb.domains.review.domain.Review;
import com.dongsan.rdb.domains.review.infrastructure.ReviewWithMemberQuery;
import com.dongsan.rdb.support.util.CursorPage;

public interface GetReviews {
    ReviewSort getSortType();

    CursorPage<ReviewWithMemberQuery> search(Long walkwayId, Review review, int size);
}
