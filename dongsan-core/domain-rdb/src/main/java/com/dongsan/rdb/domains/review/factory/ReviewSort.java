package com.dongsan.rdb.domains.review.factory;

import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;

import java.util.Arrays;

public enum ReviewSort {
    LATEST("latest"),
    RATING("rating");

    private final String type;

    ReviewSort(String type) {
        this.type = type;
    }

    public static ReviewSort typeOf(String type) {
        return Arrays.stream(ReviewSort.values())
                .filter(sort -> sort.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.INVALID_SORT_TYPE));
    }
}
