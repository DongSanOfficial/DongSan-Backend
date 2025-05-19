package com.dongsan.rdb.domains.review.domain;

import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;

import java.util.Arrays;

public enum Rating {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer num;

    Rating(Integer num) {
        this.num = num;
    }

    public Integer getNum() {
        return this.num;
    }

    public static Rating numOf(Integer num) {
        return Arrays.stream(Rating.values())
                .filter(rating -> rating.num.equals(num))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.INVALID_RATING_VALUE));
    }
}
