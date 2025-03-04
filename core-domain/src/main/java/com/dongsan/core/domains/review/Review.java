package com.dongsan.core.domains.review;

import java.time.LocalDateTime;

public record Review(
        Long reviewId,
        Reviewer reviewer,
        ReviewedWalkway reviewedWalkway,
        Rating rating,
        String content,
        LocalDateTime createdAt
) {
}
