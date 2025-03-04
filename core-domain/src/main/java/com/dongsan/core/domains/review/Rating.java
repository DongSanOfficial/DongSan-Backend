package com.dongsan.core.domains.review;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import java.util.Arrays;

public enum Rating {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5)
    ;

    private final Integer value;

    Rating(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public static Rating valueOf(Integer value) {
        return Arrays.stream(Rating.values())
                .filter(rating -> rating.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.INVALID_RATING_VALUE));
    }
}
