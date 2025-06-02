package com.dongsan.api.domains.crew;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;

import java.util.Arrays;

public enum CrewRankingPeriod {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    private final String type;

    CrewRankingPeriod(String type) {
        this.type = type;
    }

    public static CrewRankingPeriod typeOf(String type) {
        return Arrays.stream(CrewRankingPeriod.values())
                .filter(sort -> sort.type.equals(type.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.CREW_INVALID_PERIOD));
    }
}
