package com.dongsan.domain.domains.review.infrastructure;

import java.time.LocalDateTime;

public record ReviewWithWalkwayQuery(
        Long reviewId,
        Long walkwayId,
        String walkwayName,
        LocalDateTime createdAt, // 리뷰 생성 시간
        Integer rating,
        String content
) {
}
