package com.dongsan.core.domains.walkway;

public record CreateWalkwayHistory(
        Long walkwayId,
        Long memberId,
        Double distance,
        Integer time
) {
}
