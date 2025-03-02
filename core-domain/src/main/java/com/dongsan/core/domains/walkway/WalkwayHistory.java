package com.dongsan.core.domains.walkway;

import java.time.LocalDateTime;

public record WalkwayHistory(
        Long id,
        Long memberId,
        Walkway walkway,
        Double distance,
        Integer time,
        boolean isReviewed,
        LocalDateTime createdAt
) {
}
