package com.dongsan.domain.domains.review.infrastructure;

import java.time.LocalDateTime;

public record ReviewWithMemberQuery(
        Long reviewId,
        Long memberId,
        String nickname,
        LocalDateTime createdAt, // 리뷰 생성 시간
        Integer rating,
        String content
) {
}
