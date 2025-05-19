package com.dongsan.rdb.domains.review;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class GetReviewsFactory {
    private final Map<ReviewSort, GetReviews> getReviewsServiceMap = new EnumMap<>(ReviewSort.class);

    public GetReviewsFactory(List<GetReviews> getReviewsServices) {
        for (GetReviews getReviewsService : getReviewsServices) {
            this.getReviewsServiceMap.put(getReviewsService.getSortType(), getReviewsService);
        }
    }

    public GetReviews getService(ReviewSort sort) {
        return getReviewsServiceMap.get(sort);
    }
}
