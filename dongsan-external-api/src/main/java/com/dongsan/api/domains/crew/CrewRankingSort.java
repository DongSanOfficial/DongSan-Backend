package com.dongsan.api.domains.crew;

import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;

import java.util.Arrays;

public enum CrewRankingSort {
    DISTANCE("distance"),
    DURATION("duration");

    private final String type;

    CrewRankingSort(String type) {
        this.type = type;
    }

    public static CrewRankingSort typeOf(String type) {
        return Arrays.stream(CrewRankingSort.values())
                .filter(sort -> sort.type.equals(type.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new CoreException(CoreErrorCode.CREW_INVALID_SORT));
    }
}
