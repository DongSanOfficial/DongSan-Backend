package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;

import java.util.Arrays;

public enum WalkwaySort {
    LIKED("liked"),
    RATING("rating"),
    LATEST("latest");

    private final String type;

    WalkwaySort(String type) {
        this.type = type;
    }

    public static WalkwaySort typeOf(String type) {
        return Arrays.stream(WalkwaySort.values())
                .filter(sort -> sort.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.INVALID_SEARCH_TYPE));
    }
}
