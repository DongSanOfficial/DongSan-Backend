package com.dongsan.core.domains.walkway;

import java.time.LocalDateTime;

public record LikedWalkway(
        Long id,
        Long memberId,
        Long walkwayId,
        LocalDateTime createdAt
) {
}
